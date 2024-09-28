import { AuthCredentialValidatorType } from "@/lib/validators/SignupValidator";
import { useMutation, useQuery } from "react-query";
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

  const { mutateAsync: createUser, isLoading, isError, error, isSuccess } = useMutation(createUserRequest, {
    retry: false
  });

  if (isError) {
    toast.error(`${error}`);
    console.log(error)
  }

  return { createUser, isLoading, isSuccess, isError }
}

/**
 * verify user account's email
 */
const useVerifyEmailApi = (token: string) => {
  const verifyEmailRequest = async (): Promise<boolean> => {
    const response = await fetch(`${BASE_URL}/api/user/verify-email`, {
      method: 'GET',
      headers: {
        token
      },
      cache: 'no-cache'
    });
    if (!response.ok) {
      const res: ErrorResponseType = await response.json();
      throw new Error(`${res.message}`)
    }
    return response.json()
  }

  const { data: isVerified, isLoading, isError } = useQuery('verify-email', verifyEmailRequest, {
    retry: false
  });

  return { isVerified, isLoading, isError }
}

export { useCreateUserApi, useVerifyEmailApi }