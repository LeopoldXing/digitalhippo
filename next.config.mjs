/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    domains: ["digitalhippo-leopoldxing.s3.ca-central-1.amazonaws.com"],
    remotePatterns: [
      {
        protocol: "https",
        hostname: "*.s3.amazonaws.com",
      },
      {
        protocol: "http",
        hostname: "localhost"
      },
      {
        protocol: "https",
        hostname: "digitalhippo-production.up.railway.app"
      }
    ]
  }
};

export default nextConfig;
