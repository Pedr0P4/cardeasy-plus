import { type NextRequest, NextResponse } from "next/server";
import { verify } from "./services/accounts";

export async function middleware(req: NextRequest) {
  const { pathname } = req.nextUrl;
  const headers = new Headers(req.headers);
  headers.set("x-pathname", pathname);

  const token = req.cookies.get("cardeasy@token")?.value;
  const isProtectedRoute = pathname.startsWith("/teams");

  if (token && typeof token === "string") {
    try {
      const res = await verify(token);

      headers.set("x-user-account", JSON.stringify(res.data));

      if (isProtectedRoute) return NextResponse.next({ headers });
      return NextResponse.redirect(new URL("/teams", req.url), { headers });
    } catch (_) {}
  }

  req.cookies.clear();

  if (isProtectedRoute)
    return NextResponse.redirect(new URL("/", req.url), { headers });
  return NextResponse.next({ headers });
}

export const config = {
  matcher: ["/((?!api|_next/static|_next/image|favicon.ico).*)"],
};
