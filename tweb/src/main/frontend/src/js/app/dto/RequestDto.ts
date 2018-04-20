import RequestFundsDto from './RequestFundsDto';

export default class RequestDto {
    id: number;
    issueNumber: string;
    icon: string;
    owner: string;
    platform: string;
    title: string;
    status: string;
    technologies: string[];
    funds: RequestFundsDto;
    starred: boolean;
}
