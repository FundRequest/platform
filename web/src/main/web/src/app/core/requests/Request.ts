import {RequestIssueInformation} from "./RequestIssueInformation";

export class Request {

  public id: number;
  public status: string;
  public type: string;
  public technologies: string[];
  public watchers: string[];
  public loggedInUserIsWatcher: boolean;

  public balance: string;

  public issueInformation: RequestIssueInformation = new RequestIssueInformation();

}
