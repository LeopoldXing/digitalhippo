'use client'

import { Button } from './ui/button'
import Link from "next/link";
import { User } from "@/types";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import { useRouter } from "next/navigation";
import { signOut } from "@/api/UserRequest";
import { deleteCookie, getCookie } from 'cookies-next';
import { cartHooks } from "@/hooks/cartHooks";

const UserAccountNav = ({ user }: { user: User }) => {
  const router = useRouter()
  const { clearCart } = cartHooks();

  return (
      <DropdownMenu>
        <DropdownMenuTrigger asChild className='overflow-visible'>
          <Button variant='ghost' size='sm' className='relative'>My account</Button>
        </DropdownMenuTrigger>

        <DropdownMenuContent className='bg-white w-60' align='end'>
          <div className='flex items-center justify-start gap-2 p-2'>
            <div className='flex flex-col space-y-0.5 leading-none'>
              <p className='font-medium text-sm text-black'>{user.email}</p>
            </div>
          </div>
          <DropdownMenuSeparator/>
          <DropdownMenuItem asChild><Link href='/sell'>Seller Dashboard</Link></DropdownMenuItem>
          <DropdownMenuItem onClick={async () => {
            await signOut(getCookie('digitalhippo-access-token'));
            deleteCookie('digitalhippo-access-token')
            clearCart()
            router.push('/sign-in');
            router.refresh()
          }} className='cursor-pointer'>Log out</DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
  )
}

export default UserAccountNav
