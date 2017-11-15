import {makeTypedFactory, TypedRecord} from "typed-immutable-record";
import {List} from "immutable";

interface IRequestSource {
  key: String;
  value: String;
}

interface IRequestIssueInformation {
  link: String;
  owner: String;
  repo: String;
  number: Number;
  title: String;

  source: IRequestSource;
}

interface IRequest {
  id: number;
  status: string;
  type: string;
  technologies: string[];
  watchers: string[];
  loggedInUserIsWatcher: boolean;
  balance: string;
  issueInformation: IRequestIssueInformation;
}


export interface IRequestRecord extends TypedRecord<IRequestRecord>, IRequest {
}

export const createRequest = makeTypedFactory<IRequest, IRequestRecord>({
  id: 0,
  status: '',
  type: '',
  technologies: [],
  watchers: [],
  loggedInUserIsWatcher: false,
  balance: '0',
  issueInformation: {
    link: '',
    owner: '',
    repo: '',
    number: 0,
    title: '',
    source: {
      key: '',
      value: ''
    }
  }
});

export type IRequestList = List<IRequestRecord>;
