'use client';

import { useEffect, useRef, useState } from "react";
import { PRODUCT_CATEGORIES } from "@/config";
import NavItem from "@/components/NavItem";
import { useOnClickOutside } from "@/hooks/useOnClickOutside";

const NavItems = () => {
  const [activeItemIndex, setActiveItemIndex] = useState<number | null>(null);

  const navRef = useRef<HTMLDivElement | null>(null);
  useOnClickOutside(navRef, () => setActiveItemIndex(null));

  /**
   * handler: close the nav items when user press escape key
   */
  useEffect(() => {
    const handler = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        setActiveItemIndex(null);
      }
    }
    document.addEventListener('keydown', handler);
    return () => {
      document.removeEventListener('keydown', handler);
    }
  }, []);

  return (
      <div className='flex gap-4 h-full' ref={navRef}>
        {PRODUCT_CATEGORIES.map((item, index) => {
          const handleOpen = () => {
            if (activeItemIndex === index) {
              setActiveItemIndex(null);
            } else {
              setActiveItemIndex(index);
            }
          }

          return (
              <NavItem key={item.value} handleOpen={handleOpen} isOpen={activeItemIndex === index} isAnyOpen={!!activeItemIndex}
                       category={item} close={() => setActiveItemIndex(null)} />
          )
        })}
      </div>
  );
};

export default NavItems;