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
    technologies: string[];
    funds: RequestFundsDto;
    starred: boolean;
}
