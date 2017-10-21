import {RequestIssueInformation} from "./RequestIssueInformation";
import {Serializable} from "./Serializable";

export class Request extends Serializable {

  public id: Number;
  public status: String;
  public type: String;
  public technologies: String[];
  public watchers: String[];
  public loggedInUserIsWatcher: Boolean;

  public issueInformation: RequestIssueInformation = new RequestIssueInformation();
}
