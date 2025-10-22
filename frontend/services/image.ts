import { Service } from "./base/services";

export type ImageData = {
  url: string;
  blob: Blob;
  base64: string;
  filename?: string;
};

export class ImagesService extends Service {
  async blobToBase64(blob: Blob): Promise<string> {
    return await new Promise<string>((resolve, reject) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
      reader.readAsDataURL(blob);
    });
  }

  async urlToData(url: string): Promise<ImageData> {
    return this.api
      .get<Blob>(url, {
        responseType: "blob",
      })
      .then(async (response) => {
        const blob = response.data;
        const base64 = await this.blobToBase64(blob);
        return {
          url,
          blob,
          base64,
        };
      });
  }
}
