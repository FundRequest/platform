import RequestFundsDto from './RequestFundsDto';

export class RequestIssueInformationDto {
    owner: string;
    repo: string;
    number: number;
    title: string;
    platform: string;
    platformId: string;
    url: string;
}

export class RequestListItemPendingFundDto {
    id: string;
    description: string;
    transactionId: string;
    fromAddress: string;
    amount: string;
    tokenAddress: string;
    issueInformation: RequestIssueInformationDto;
    funds: RequestFundsDto;
}