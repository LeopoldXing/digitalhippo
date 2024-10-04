import { cn, constructMetadata } from '@/lib/utils'
import { Inter } from 'next/font/google'
import { Toaster } from 'sonner'
import './globals.css'
import React, { ReactNode } from "react";
import Navbar from "@/components/Navbar";
import ReactQueryClientProvider from "@/lib/ReactQueryClientProvider";
import Providers from "@/components/Providers";

const inter = Inter({ subsets: ['latin'] })

export const metadata = constructMetadata()

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
      <html lang='en' className='h-full'>
      <body className={cn('relative h-full font-sans antialiased', inter.className)}>
      <ReactQueryClientProvider>
        <main className='relative flex flex-col min-h-screen'>
          <Providers>
            <Navbar/>
            <div className="flex-grow flex-1">{children}</div>
          </Providers>
        </main>
      </ReactQueryClientProvider>
      <Toaster position='top-center' richColors/>
      </body>
      </html>
  )
}
