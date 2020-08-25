export interface IMedia {
    type: string;
    contentType: string;
}

export class Media implements IMedia {

    type: string;
    contentType: string;

    constructor(type?: string, contentType?: string) {
        this.type = type ? type : null;
        this.contentType = contentType ? contentType : null;
    }
}
