'use client';

import { Icons } from '@/components/Icons'
import Link from "next/link";
import { Button, buttonVariants } from "@/components/ui/button";
import { ArrowRight } from "lucide-react";
import { Label } from "@/components/ui/label";
import { cn } from "@/lib/utils";
import { Input } from "@/components/ui/input";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AuthCredentialValidator, AuthCredentialValidatorType } from "@/lib/validators/SignupValidator";
import { useCreateUserApi } from "@/api/UserApi";

const Page = () => {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<AuthCredentialValidatorType>({ resolver: zodResolver(AuthCredentialValidator) });

  const { createUser } = useCreateUserApi();
  const handleSignUp = async ({ email, password }: AuthCredentialValidatorType) => {
    const res = await createUser({ email, password });
    console.log(res);
  }

  return (
      <>
        <div className='container relative flex pt-20 flex-col items-center justify-center lg:px-0'>
          <div className='mx-auto flex w-full flex-col justify-center space-y-6 sm:w-[350px]'>
            <div className='flex flex-col items-center space-y-2 text-center'>
              <Icons.logo className='h-20 w-20'/>
              <h1 className='text-2xl font-semibold tracking-tight'>
                Create an account
              </h1>
              <Link href='/sign-in' className={buttonVariants({ variant: 'link', className: 'gap-1.5' })}>
                Already have an account? Sign-in
                <ArrowRight className='h-4 w-4'/>
              </Link>
            </div>

            <div className="grid gap-6">
              <form onSubmit={handleSubmit(handleSignUp)}>
                <div className="grid gap-2">
                  <div className="py-2 grid gap-1">
                    <Label htmlFor='email'>Email</Label>
                    <Input {...register('email')} type='email' className={cn({ "focus-visible:ring-red-500": errors.email })}
                           placeholder='xxx@example.com'/>
                  </div>
                  <div className="py-2 grid gap-1">
                    <Label htmlFor='password'>Password</Label>
                    <Input {...register('password')} type='password' className={cn({ "focus-visible:ring-red-500": errors.password })}
                           placeholder='password'/>
                  </div>
                  <Button>Sign up</Button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </>
  );
};

export default Page;