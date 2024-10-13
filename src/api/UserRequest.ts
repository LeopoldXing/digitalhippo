import { toast } from "sonner";
import { ErrorResponseType, User } from "@/types";

const BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL;
const FRONTEND_ENDPOINT = process.env.NEXT_PUBLIC_FRONTEND_URL;

/**
 * query user info by access token
 */
const getUserRequest = async (accessToken: string | undefined): Promise<User | undefined> => {
  const response = await fetch(`${BASE_URL}/api/user`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${accessToken}`
    }
  });
  if (!response.ok) {
    return
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
const createUserRequest = async ({ email, password, productIdList, payloadId }: {
  email: string,
  password: string,
  productIdList: string[] | undefined | null,
  payloadId: string
}): Promise<User | null> => {
  const response = await fetch(`${BASE_URL}/api/user/sign-up`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ email, password, productIdList, payloadId })
  })
  if (!response.ok) {
    const res: ErrorResponseType = await response.json();
    throw new Error(`${res.message || 'Failed to sign up, please try again.'}`)
  }
  return response.json()
}

/**
 * sign in payload
 * @param email
 * @param password
 */
const payloadSignIn = async ({ email, password }: { email: string, password: string }) => {
  const response = await fetch(`${FRONTEND_ENDPOINT}/api/users/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ email, password })
  });
  if (!response.ok) {
    throw new Error("Failed to login Payload")
  }
  return response.json()
}

export { getUserRequest, signOut, createUserRequest, payloadSignIn }