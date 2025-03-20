'use client';

import { Icons } from '@/components/Icons'
import Link from "next/link";
import { Button, buttonVariants } from "@/components/ui/button";
import { ArrowRight } from "lucide-react";
import { cn } from "@/lib/utils";
import { Input } from "@/components/ui/input";
import { FormProvider, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AuthCredentialsValidator, AuthCredentialValidatorType } from "@/lib/validators/SignupValidator";
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useRouter, useSearchParams } from "next/navigation";
import { setCookie } from "cookies-next";
import { useSignIn } from "@/hooks/authHooks";
import { payloadSignIn } from "@/api/UserRequest";
import { cartHooks } from "@/hooks/cartHooks";

const Page = () => {
  const router = useRouter();
  const searchParams = useSearchParams();
  const isSeller = searchParams.get('as') === 'seller'

  // redirect user to where they are before after signing in
  const origin = searchParams.get('origin')

  const form = useForm<AuthCredentialValidatorType>({
    resolver: zodResolver(AuthCredentialsValidator),
    defaultValues: {
      email: "",
      password: "",
    }
  });

  const continueAsSeller = () => {
    router.push('?as=seller')
  }
  const continueAsBuyer = () => {
    router.push('/sign-in', undefined)
  }

  /*  sign in  */
  const { signIn, isLoading } = useSignIn()
  const { getItems, clearCart, addItem } = cartHooks();
  const handleSignIn = async ({ email, password }: AuthCredentialValidatorType) => {
    const { accessToken, productList } = await signIn({
      email,
      password,
      isSeller,
      productIdList: getItems()?.map(cartItem => cartItem.product.id!)
    });
    const expirationTime = new Date();
    expirationTime.setHours(expirationTime.getHours() + 1);
    // save access token into cookie
    setCookie('digitalhippo-access-token', accessToken, { expires: expirationTime })

    // reset cart
    clearCart()
    productList?.forEach(product => addItem({ product, accessToken: undefined, sendNotification: false }))

    // payload sign in
    await payloadSignIn({ email, password })

    if (isSeller) {
      router.push('/sell')
      router.refresh();
      return
    }
    if (origin) {
      // redirect user to where they were before signing in
      router.push(`${origin}`)
      router.refresh();
      return
    }
    router.push('/')
    router.refresh();
  }

  return (
      <>
        <div className='container relative flex pt-20 flex-col items-center justify-center lg:px-0'>
          <div className='mx-auto flex w-full flex-col justify-center space-y-6 sm:w-[350px]'>
            <div className='flex flex-col items-center space-y-2 text-center'>
              <Icons.logo className='h-20 w-20'/>
              <h1 className='text-2xl font-semibold tracking-tight'>
                Sign in to your {isSeller ? 'seller' : ''}{' '}account
              </h1>
              <Link href='/sign-up' className={buttonVariants({ variant: 'link', className: 'gap-1.5' })}>
                Don&apos;t have an account? <ArrowRight className='h-4 w-4'/>
              </Link>
            </div>

            <div className="grid gap-6">
              <FormProvider {...form}>
                <form onSubmit={form.handleSubmit(handleSignIn)} className="grid gap-2">
                  <FormField control={form.control} name='email' render={({ field }) => (
                      <FormItem className="py-2 grid gap-1">
                        <FormLabel>Email</FormLabel>
                        <FormControl>
                          <Input {...field} type='email' className={cn({ "focus-visible:ring-red-500": form.formState.errors.email })}
                                 placeholder='Email'/>
                        </FormControl>
                        <FormMessage/>
                      </FormItem>
                  )}/>
                  <FormField control={form.control} name='password' render={({ field }) => (
                      <FormItem className="py-2 grid gap-1">
                        <FormLabel>Password</FormLabel>
                        <FormControl>
                          <Input {...field} type='password'
                                 className={cn({ "focus-visible:ring-red-500": form.formState.errors.password })}
                                 placeholder='password'/>
                        </FormControl>
                        <FormMessage/>
                      </FormItem>
                  )}/>
                  <Button type='submit'>Sign in</Button>
                </form>
              </FormProvider>
              <div className='relative'>
                <div aria-hidden={true} className='absolute inset-0 flex items-center'>
                  <span className='w-full border-t'/>
                </div>
                <div className='relative flex justify-center text-xs uppercase'>
                  <span className='bg-background px-2 text-muted-foreground'>or</span>
                </div>
              </div>
              {isSeller ? (
                  <Button onClick={continueAsBuyer} variant='secondary' disabled={isLoading}>Continue as customer</Button>
              ) : (
                  <Button onClick={continueAsSeller} variant='secondary' disabled={isLoading}>Continue as seller</Button>
              )}
            </div>
          </div>
        </div>
      </>
  );
};

export default Page;