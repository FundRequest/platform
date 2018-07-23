import RequestFundsDto from './RequestFundsDto';

export default class RequestDto {
    id: number;
    icon: string;
    owner: string;
    platform: string;
    repo: string;
    issueNumber: string;
    title: string;
    description: string;
    status: string;
    phase: string;
    technologies: string[];
    funds: RequestFundsDto;
    starred: boolean;
    lastModifiedDate: Date;
}
