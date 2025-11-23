import { Service } from "./base/services";

export type Attachment = {
  id: number;
  size: number;
  filename: string;
};

export type CreateAttachmentData = {
  file?: File;
  filename: string;
  card: number;
};

export type UpdateAttachmentData = {
  file?: File;
  filename: string;
};

export class AttachmentsService extends Service {
  async create({ file, filename, ...data }: CreateAttachmentData) {
    const form = new FormData();

    form.append("file", new Blob(file ? [file] : []), filename);

    form.append(
      "body",
      new Blob([JSON.stringify(data)], {
        type: "application/json",
      }),
    );

    return this.api.post<string>("/attachments", form, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }

  async get(id: number) {
    return this.api
      .get<Attachment>(`/attachments/${id}`)
      .then((res) => res.data);
  }

  async update(id: number, { file, filename }: UpdateAttachmentData) {
    const form = new FormData();

    form.append("file", new Blob(file ? [file] : []), filename);
    console.log(file, filename);
    return this.api.put<string>(`/attachments/${id}`, form, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }

  async delete(id: number) {
    return this.api.delete(`/attachments/${id}`);
  }

  async search(card: number, page: number = 0, query: string = "") {
    return this.api
      .get<Page<Attachment>>(
        `/attachments/search?card=${card}&page=${page}&query=${query}`,
      )
      .then((res) => res.data);
  }

  async download(id: number) {
    return this.api
      .get<File>(`attachments/${id}/download`, {
        responseType: "blob",
      })
      .then(
        (response) => new File([response.data], response.headers["filename"], {
          type: "application/pdf",
        })
      );
  }
}
