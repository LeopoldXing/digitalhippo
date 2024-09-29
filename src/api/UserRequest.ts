import { toast } from "sonner";

const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL;

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

export { getUserRequest, signOut }