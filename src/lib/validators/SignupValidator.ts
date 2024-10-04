import { z } from "zod";

const AuthCredentialsValidator = z.object({
  email: z.string().min(0, "Email can not be empty").email("Email must be valid"),
  password: z.string().min(8, { message: 'Password must be at least 8 characters' }),
})

export type AuthCredentialValidatorType = z.infer<typeof AuthCredentialsValidator>

export { AuthCredentialsValidator };
