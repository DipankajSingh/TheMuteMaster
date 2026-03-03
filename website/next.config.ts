import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  compress: true,
  poweredByHeader: false,
  generateEtags: true,
  reactStrictMode: true,
  headers: async () => {
    return [
      {
        source: '/:path*',
        headers: [
          {
            key: 'X-Content-Type-Options',
            value: 'nosniff'
          },
          {
            key: 'X-Frame-Options',
            value: 'SAMEORIGIN'
          },
          {
            key: 'X-XSS-Protection',
            value: '1; mode=block'
          }
        ]
      }
    ];
  },
  redirects: async () => {
    return [
      {
        source: '/sitemap.xml',
        destination: '/sitemap.xml',
        permanent: true,
      },
      {
        source: '/robots.txt',
        destination: '/robots.txt',
        permanent: true,
      }
    ];
  }
};

export default nextConfig;

