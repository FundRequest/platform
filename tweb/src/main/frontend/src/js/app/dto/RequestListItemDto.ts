import FundsDto from './FundsDto';

export default class RequestsListItemDto {
    icon: string;
    owner: string;
    platform: string;
    title: string;
    status: string;
    technologies: string[];
    fndFunds: FundsDto;
    otherFunds: FundsDto;
    starred: boolean;
}