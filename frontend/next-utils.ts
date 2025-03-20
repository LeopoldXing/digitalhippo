import next from "next"

const PORT = Number(process.env.NEXT_PUBLIC_APPLICATION_PORT) || 443

export const nextApp = next({
  dev: process.env.NODE_ENV !== "production",
  port: PORT
})

export const nextHandler = nextApp.getRequestHandler()
