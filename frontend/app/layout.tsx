import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Cardeasy+",
  description: "A nova versão do Cardeasy!",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html data-theme="synthwave" lang="en">
      <body className="antialiased">{children}</body>
    </html>
  );
}
