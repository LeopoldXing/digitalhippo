'use client';

import React from 'react';
import { QueryClient, QueryClientProvider } from "react-query";

const ReactQueryClientProvider = ({ children }: { children: React.ReactNode }) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false
      }
    }
  });

  return (
      <QueryClientProvider client={queryClient}>
        {children}
      </QueryClientProvider>
  );
};

export default ReactQueryClientProvider;