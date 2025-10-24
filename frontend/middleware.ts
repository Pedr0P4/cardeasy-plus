import { getCookie } from "cookies-next/server";
import { type NextRequest, NextResponse } from "next/server";
import { Api } from "./services/api";

export async function middleware(req: NextRequest) {
  const { pathname } = req.nextUrl;
  const headers = new Headers(req.headers);
  headers.set("x-pathname", pathname);

  const token = await getCookie("cardeasy@token", { req });
  const isProtectedRoute = pathname.startsWith("/home");

  if (token && typeof token === "string") {
    try {
      const res = await Api.server().accounts().verify();

      headers.set("x-user-account", JSON.stringify(res.data));

      if (isProtectedRoute) return NextResponse.next({ headers });
      return NextResponse.redirect(new URL("/home", req.url), { headers });
    } catch (_) {}
  }

  req.cookies.clear();

  if (isProtectedRoute)
    return NextResponse.redirect(new URL("/login", req.url), { headers });
  return NextResponse.next({ headers });
}

export const config = {
  matcher: ["/((?!api|_next/static|_next/image|favicon.ico).*)"],
};
