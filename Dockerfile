FROM node:18-alpine

WORKDIR /app

# Install dependencies based on the preferred package manager
COPY package.json yarn.lock* package-lock.json* pnpm-lock.yaml* ./
# Omit --production flag for TypeScript devDependencies
RUN \
  if [ -f yarn.lock ]; then yarn --frozen-lockfile; \
  elif [ -f package-lock.json ]; then npm ci; \
  elif [ -f pnpm-lock.yaml ]; then corepack enable pnpm && pnpm i; \
  else echo "Warning: Lockfile not found. It is recommended to commit lockfiles to version control." && yarn install; \
  fi

# Copy environment variables file
COPY .env.local ./

COPY . .

# Environment variables must be present at build time
ARG NEXT_PUBLIC_APPLICATION_PORT
ARG NEXT_PUBLIC_FRONTEND_URL
ARG NEXT_PUBLIC_BACKEND_URL
ARG PAYLOAD_SECRET
ARG MONGODB_URL
ARG ACCESS_KEY
ARG SECRET_KEY
ARG S3_ENDPOINT_HOST
ARG S3_BUCKET

ENV NEXT_PUBLIC_APPLICATION_PORT=${NEXT_PUBLIC_APPLICATION_PORT}
ENV NEXT_PUBLIC_FRONTEND_URL=${NEXT_PUBLIC_FRONTEND_URL}
ENV NEXT_PUBLIC_BACKEND_URL=${NEXT_PUBLIC_BACKEND_URL}
ENV PAYLOAD_SECRET=${PAYLOAD_SECRET}
ENV MONGODB_URL=${MONGODB_URL}
ENV ACCESS_KEY=${ACCESS_KEY}
ENV SECRET_KEY=${SECRET_KEY}
ENV S3_ENDPOINT=${S3_ENDPOINT_HOST}
ENV S3_BUCKET=${S3_BUCKET}

# Disable Next.js telemetry (optional)
# ENV NEXT_TELEMETRY_DISABLED 1

# Build Next.js based on the preferred package manager
RUN \
  if [ -f yarn.lock ]; then yarn build; \
  elif [ -f package-lock.json ]; then npm run build; \
  elif [ -f pnpm-lock.yaml ]; then pnpm build; \
  else npm run build; \
  fi

# Start Next.js based on the preferred package manager
CMD \
  if [ -f yarn.lock ]; then yarn start; \
  elif [ -f package-lock.json ]; then npm run start; \
  elif [ -f pnpm-lock.yaml ]; then pnpm start; \
  else npm run start; \
  fi
