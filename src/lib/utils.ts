import { type ClassValue, clsx } from 'clsx'
import { Metadata } from 'next'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatPrice(
    price: number | string,
    options: {
      currency?: 'CAD' | 'EUR' | 'GBP' | 'USD'
      notation?: Intl.NumberFormatOptions['notation']
      locale?: string
    } = {}
) {
  // eslint-disable-next-line no-unused-vars,@typescript-eslint/no-unused-vars
  const { currency = 'CAD', notation = 'compact', locale = 'en-US' } = options

  const numericPrice = typeof price === 'string' ? parseFloat(price) : price
  if (isNaN(numericPrice)) {
    throw new Error('Invalid price input');
  }

  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency,
    notation,
    maximumFractionDigits: 2,
  }).format(numericPrice)
}

export function constructMetadata({
                                    title = 'DigitalHippo - the marketplace for digital assets',
                                    description = 'DigitalHippo is an open-source marketplace for high-quality digital goods.',
                                    image = '/thumbnail.png',
                                    icons = '/favicon.ico',
                                    noIndex = false,
                                  }: {
  title?: string
  description?: string
  image?: string
  icons?: string
  noIndex?: boolean
} = {}): Metadata {
  return {
    title,
    description,
    openGraph: {
      title,
      description,
      images: [
        {
          url: image,
        },
      ],
    },
    twitter: {
      card: 'summary_large_image',
      title,
      description,
      images: [image],
      creator: '@joshtriedcoding',
    },
    icons,
    metadataBase: new URL('https://digitalhippo.up.railway.app'),
    ...(noIndex && {
      robots: {
        index: false,
        follow: false,
      },
    }),
  }
}
