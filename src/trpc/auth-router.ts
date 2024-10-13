import { AuthCredentialsValidator } from "../lib/validators/SignupValidator";
import { publicProcedure, router } from './trpc'
import { getPayloadClient } from '../get-payload'
import { TRPCError } from '@trpc/server'
import { z } from 'zod'

export const authRouter = router({
  createPayloadUser: publicProcedure
      .input(AuthCredentialsValidator)
      .mutation(async ({ input }) => {
        const { email, password } = input
        const payload = await getPayloadClient()

        // check if user already exists
        const { docs: users } = await payload.find({
          collection: 'users',
          where: {
            email: { equals: email }
          }
        })

        if (users.length !== 0) throw new TRPCError({ code: 'CONFLICT' })

        const newUser = await payload.create({
          collection: 'users',
          data: {
            email,
            password,
            role: 'user'
          }
        })

        return { success: true, sentToEmail: email, payloadUserId: newUser.id }
      }),

  verifyEmail: publicProcedure
      .input(z.object({ token: z.string() }))
      .query(async ({ input }) => {
        const { token } = input

        const payload = await getPayloadClient()

        const isVerified = await payload.verifyEmail({
          collection: 'users',
          token,
        })

        if (!isVerified)
          throw new TRPCError({ code: 'UNAUTHORIZED' })

        return { success: true }
      }),

  signIn: publicProcedure
      .input(AuthCredentialsValidator)
      .mutation(async ({ input, ctx }) => {
        const { email, password } = input
        const { res } = ctx

        const payload = await getPayloadClient()

        await payload.login({
          collection: 'Users',
          data: {
            email,
            password
          },
          res
        })

        return { success: true }
      })
})
