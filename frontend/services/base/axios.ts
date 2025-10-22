import axios, { type AxiosError } from "axios";

export type ApiErrorResponse = (ErrorResponse | ValidationErrorResponse) & {
  isValidationError: () => this is ValidationErrorResponse;
  isErrorResponse: () => this is ErrorResponse;
  isApiError: () => this is ApiErrorResponse;
};

export type ErrorResponse = {
  status: number;
  error: string;
};

export type ValidationErrorResponse = {
  status: number;
  errors: Record<string, string>;
};

// biome-ignore lint/suspicious/noExplicitAny: is a type guard
function isValidationError(data: any): data is ValidationErrorResponse {
  return data && typeof data.errors === "object" && data.errors !== null;
}

// biome-ignore lint/suspicious/noExplicitAny: is a type guard
function isErrorResponse(data: any): data is ErrorResponse {
  return data && typeof data.error === "string" && data.error !== null;
}

// biome-ignore lint/complexity/noStaticOnlyClass: <explanation>
export class Axios {
  public static create() {
    const api = axios.create({
      baseURL: "http://localhost:8080",
    });

    api.interceptors.response.use(
      (res) => res,
      (err: AxiosError) => {
        return Promise.reject({
          ...(err.response?.data ?? {}),
          isErrorResponse: () => isErrorResponse(err.response?.data),
          isValidationError: () => isValidationError(err.response?.data),
          isApiError: () =>
            isErrorResponse(err.response?.data) ||
            isValidationError(err.response?.data),
        } as ApiErrorResponse);
      },
    );

    return api;
  }
}
