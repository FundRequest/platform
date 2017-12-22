import {makeTypedFactory, TypedRecord} from 'typed-immutable-record';
import {List} from 'immutable';

interface IRequestIssueInformation {
  link: string;
  owner: string;
  repo: string;
  number: Number;
  title: string;

  platform: string;
  platformId: string;
}

interface IRequest {
  id: number;
  status: string;
  type: string;
  technologies: string[];
  watchers: string[];
  loggedInUserIsWatcher: boolean;
  balance: string;
  funderBalance: string;
  numberOfFunders: string;
  issueInformation: IRequestIssueInformation;
}


export interface IRequestRecord extends TypedRecord<IRequestRecord>, IRequest {
}

export const createRequest = makeTypedFactory<IRequest, IRequestRecord>({
  id                   : 0,
  status               : '',
  type                 : '',
  technologies         : [],
  watchers             : [],
  loggedInUserIsWatcher: false,
  balance              : '0',
  funderBalance              : '0',
  numberOfFunders: '0',
  issueInformation     : {
    link  : '',
    owner : '',
    repo  : '',
    number: 0,
    title : '',
    platform: '',
    platformId  : ''
  }
});

export type IRequestList = List<IRequestRecord>;

export class FundRequestCommand {
  constructor(public platform: string, public platformId: string, public link: string, public amount: number) {
  }
}

export class FundInfo {
  constructor(public numberOfFunders: string, public balance: string, public funderBalance: string) {
  }
}
