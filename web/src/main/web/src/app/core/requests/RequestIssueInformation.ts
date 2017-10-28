import {RequestSource} from "./RequestSource";

export class RequestIssueInformation {

  public link: String;
  public owner:  String;
  public repo: String;
  public number: Number;
  public title: String;

  public technologies: String[];
  public watchers: String[];

  public source: RequestSource = new RequestSource();

}
