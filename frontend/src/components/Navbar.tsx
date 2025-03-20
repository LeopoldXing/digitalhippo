import React from 'react';
import MaxWidthWrapper from "@/components/MaxWidthWrapper";
import Link from "next/link";
import { Icons } from "@/components/Icons";
import NavItems from "@/components/NavItems";
import { buttonVariants } from "@/components/ui/button";
import Cart from "@/components/Cart";
import { cookies } from "next/headers";
import { getUserRequest } from "@/api/UserRequest";
import UserAccountNav from "@/components/UserAccountNav";
import { User } from "@/types";

const Navbar = async () => {
  const cookieStore = cookies()
  const accessToken = cookieStore.get('digitalhippo-access-token')?.value
  const user: User | undefined = await getUserRequest(accessToken);

  return (
      <div className="bg-white sticky z-50 top-0 inset-x-0 h-16">
        <header className="relative bg-white">
          <MaxWidthWrapper>
            <div className="border-b border-gray-200">
              <div className="flex h-16 items-center">
                <div className="ml-4 flex lg:ml-0">
                  <Link href='/'>
                    <Icons.logo className="h-10 w-10"/>
                  </Link>
                </div>
                <div className='hidden z-50 lg:ml-8 lg:block lg:self-stretch'>
                  <NavItems/>
                </div>

                <div className="ml-auto flex items-center">
                  <div className="flex flex-1 items-center justify-end space-x-6">
                    {user ? (
                        <UserAccountNav user={user}/>
                    ) : (
                        <>
                          {/*  logged out  */}
                          <Link href='/sign-in' className={buttonVariants({ variant: 'ghost' })}>Sign in</Link>
                          <span className='h-6 w-px bg-gray-200' aria-hidden='true'/>
                          <Link href='/sign-up' className={buttonVariants({ variant: 'ghost' })}>Create account</Link>
                          <div className="flex lg:ml-6">
                            <span className="h-6 w-px bg-gray-200" aria-hidden={true}></span>
                          </div>
                        </>
                    )}
                    <div className='ml-4 flow-root lg:ml-6'>
                      <Cart user={user}/>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </MaxWidthWrapper>
        </header>

      </div>
  );
};

export default Navbar;