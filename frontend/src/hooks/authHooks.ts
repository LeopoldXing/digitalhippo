import { useMutation, useQuery } from "react-query";
import { toast } from "sonner";
import { ErrorResponseType, ProductApiType } from "@/types";

const BASE_URL = process.env.BACKEND_URL;

/**
 * user sign in api
 */
const useSignIn = () => {
  const signInRequest = async (params: {
    email: string,
    password: string,
    isSeller: boolean,
    productIdList: string[] | undefined | null
  }): Promise<{ accessToken: string, productList: ProductApiType[] }> => {
    const response = await fetch(`${BASE_URL}/api/user/sign-in`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(params)
    });
    if (!response.ok) {
      const res: ErrorResponseType = await response.json();
      throw new Error(`${res.message || 'Failed to sign in, please try again.'}`)
    }
    return response.json();
  }

  const { mutateAsync: signIn, isLoading, isError, error, isSuccess } = useMutation(signInRequest, {
    retry: false,
    onError: (error) => {
      toast.error(`${error}`)
    },
  })

  return { signIn, isLoading, isError, error, isSuccess }
}

/**
 * verify user account's email
 */
const useVerifyEmail = (token: string) => {
  const verifyEmailRequest = async (): Promise<boolean> => {
    const response = await fetch(`${BASE_URL}/api/user/verify-email`, {
      method: 'GET',
      headers: {
        token
      }
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

export { useVerifyEmail, useSignIn }