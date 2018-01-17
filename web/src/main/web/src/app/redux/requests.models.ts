import {makeTypedFactory, TypedRecord} from 'typed-immutable-record';
import {List} from 'immutable';

interface RequestIssueInformation {
  link: string;
  owner: string;
  repo: string;
  number: Number;
  title: string;

  platform: string;
  platformId: string;
}

export interface RequestIssueFundInformation {
  numberOfFunders: string;
  balance: string;
  funderBalance: string;
}

export interface IRequest {
  id: number;
  status: string;
  type: string;
  technologies: string[];
  watchers: string[];
  loggedInUserIsWatcher: boolean;
  issueInformation: RequestIssueInformation;
  fundInfo: RequestIssueFundInformation
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
  issueInformation     : {
    link  : '',
    owner : '',
    repo  : '',
    number: 0,
    title : '',
    platform: '',
    platformId  : ''
  },
  fundInfo : {
    numberOfFunders: '0',
    balance: '0',
    funderBalance: '0'
  }
});

export type IRequestList = List<IRequestRecord>;

export class FundRequestCommand {
  constructor(public platform: string, public platformId: string, public link: string, public amount: number) {
  }
}

export class ClaimRequestCommand {
  constructor(public id:number, public platform: string, public platformId: string, public solverAddress: string) {
  }
}

export class SignedClaim {
  constructor(public platform: string, public platformId: string, public solverAddress: string, public solver: string, public r: string, public s: string, public v: number) {
  }
}
