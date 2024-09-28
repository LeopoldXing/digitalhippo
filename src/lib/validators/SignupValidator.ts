import { z } from "zod";

const AuthCredentialValidator = z.object({
  email: z.string().email("Email must be in a valid form"),
  password: z.string().min(8, { message: 'Password must be at least 8 characters' }),
})

export type AuthCredentialValidatorType = z.infer<typeof AuthCredentialValidator>

export { AuthCredentialValidator };
