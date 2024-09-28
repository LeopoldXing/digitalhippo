import { AuthCredentialValidatorType } from "@/lib/validators/SignupValidator";
import { useMutation } from "react-query";
import { toast } from "sonner";
import { ErrorResponseType, User } from "@/types";

const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;

/**
 * create new user api
 */
const useCreateUserApi = () => {
  const createUserRequest = async (params: AuthCredentialValidatorType): Promise<User | null> => {
    const response = await fetch(`${BASE_URL}/api/user`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(params),
      cache: 'no-cache'
    })
    if (!response.ok) {
      const res: ErrorResponseType = await response.json();
      throw new Error(`${res.message}`)
    }
    return response.json()
  }

  const { mutateAsync: createUser, isLoading, isError, error, isSuccess } = useMutation(createUserRequest);

  if (isError) {
    toast.error(`${error}`);
  }
  if (isSuccess) {
    toast.success("Verification email sent")
  }

  return { createUser, isLoading }
}

export { useCreateUserApi }