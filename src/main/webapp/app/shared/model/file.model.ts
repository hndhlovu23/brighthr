export interface IFile {
  id?: number;
  filetypeId?: number;
  folderId?: number;
  name?: string;
  typeName?: string;
  foldername?: string;
  fileUrl?: string;
  added?: string;
  size?: number;
}

export const defaultValue: Readonly<IFile> = {};
