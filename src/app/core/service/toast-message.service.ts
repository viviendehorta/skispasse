import {Injectable} from '@angular/core';
import {MessageService} from "primeng/api";

@Injectable()
export class ToastMessageService {

    readonly defaultLifeTimeInMillis: number = 5000;

    constructor(private readonly messageService: MessageService) {
    }

    displayError(message: string, lifeTimeInMillis: number = this.defaultLifeTimeInMillis): void {
        this.messageService.add({
            detail: message,
            severity: "error",
            life: lifeTimeInMillis
        });
    }

    displaySuccess(message: string, lifeTimeInMillis: number = this.defaultLifeTimeInMillis): void {
        this.messageService.add({
            detail: message,
            severity: "success",
            life: lifeTimeInMillis
        });
    }
}
