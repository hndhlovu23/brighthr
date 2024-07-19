import { IFile } from 'app/shared/model/file.model';

export interface IFolder {
  id?: number;
  name?: string;
  files?: IFile[] | null;
}

export const defaultValue: Readonly<IFolder> = {};
