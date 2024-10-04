import { toast } from "sonner";
import { AuthCredentialValidatorType } from "@/lib/validators/SignupValidator";
import { ErrorResponseType, User } from "@/types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

/**
 * query user info by access token
 */
const getUserRequest = async (accessToken: string | undefined) => {
  const response = await fetch(`${BASE_URL}/api/user`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${accessToken}`
    }
  });
  if (!response.ok) {
    return undefined
  }
  return response.json();
}

/**
 * sign out request
 * @param accessToken
 */
const signOut = async (accessToken: string | undefined) => {
  try {
    const response = await fetch(`${BASE_URL}/api/user/sign-out`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        "Authorization": `Bearer ${accessToken}`
      }
    })
    if (!response.ok) {
      throw new Error("Sign Out Failed")
    }
    toast.success("Sign out successfully.")
  } catch (error) {
    toast.error(`${error}`)
  }
}

/**
 * create new user api
 */
const createUserRequest = async (params: AuthCredentialValidatorType): Promise<User | null> => {
  const response = await fetch(`${BASE_URL}/api/user/sign-up`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(params)
  })
  if (!response.ok) {
    const res: ErrorResponseType = await response.json();
    throw new Error(`${res.message || 'Failed to sign up, please try again.'}`)
  }
  return response.json()
}

export { getUserRequest, signOut, createUserRequest }