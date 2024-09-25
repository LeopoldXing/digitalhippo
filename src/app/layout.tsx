import { cn, constructMetadata } from '@/lib/utils'
import { Inter } from 'next/font/google'
import { Toaster } from 'sonner'
import './globals.css'
import React from "react";

const inter = Inter({ subsets: ['latin'] })

export const metadata = constructMetadata()

export default function RootLayout() {
  return (
      <html lang='en' className='h-full'>
      <body className={cn('relative h-full font-sans antialiased', inter.className)}>
      <main className='relative flex flex-col min-h-screen'>

      </main>

      <Toaster position='top-center' richColors/>
      </body>
      </html>
  )
}
