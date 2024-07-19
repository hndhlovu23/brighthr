import { IFile } from 'app/shared/model/file.model';

export interface IFiletype {
  id?: number;
  type?: string;
  files?: IFile[] | null;
}

export const defaultValue: Readonly<IFiletype> = {};
