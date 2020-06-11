import {INewsFactDetail} from "../../../shared/model/news-fact-detail.model";

export interface INewsFactWithFile {
  newsFact: INewsFactDetail;
  file: File;
}

export class NewsFactWithFile implements INewsFactWithFile{
  private _newsFact: INewsFactDetail;
  private _file: File;

  constructor(newsFact: INewsFactDetail, file: File) {
    this._newsFact = newsFact;
    this._file = file;
  }


  get newsFact(): INewsFactDetail {
    return this._newsFact;
  }

  get file(): File {
    return this._file;
  }
}
