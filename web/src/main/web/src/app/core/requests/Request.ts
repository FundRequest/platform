import {RequestIssueInformation} from "./RequestIssueInformation";
import {Serializable} from "./Serializable";
import {ContractService} from "../contracts/contracts.service";
import {Observable} from "rxjs/Observable";
import {ChangeDetectorRef} from "@angular/core";

export class Request extends Serializable {

  private _id: number;
  public status: string;
  public type: string;
  public technologies: string[];
  public watchers: string[];
  public loggedInUserIsWatcher: boolean;

  private _balance: string;

  public issueInformation: RequestIssueInformation = new RequestIssueInformation();

  public set balance(balance: string) {
    this._balance = balance;
  }

  public get balance() {
    return this._balance;
  }

  public set id(id: number) {
    this._id = id;

    if (this._balance == null) {
      const contract: ContractService = new ContractService();

      new Observable(observer => {
        contract.getRequestBalance(this.id, function (err, result: Number) {
          observer.next(result);
          observer.complete();
        });
      }).subscribe(value => this.balance = Number.parseFloat(value.toString()).toFixed(2));
    }
  }

  public get id() {
    return this._id;
  }
}
