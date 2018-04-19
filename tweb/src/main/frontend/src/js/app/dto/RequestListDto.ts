import RequestListItemDto from './RequestListItemDto';
import {Utils} from '../utils';

export default class RequestsListDto {
    private requests: RequestListItemDto[] = [];

    constructor(requests: RequestListItemDto[]) {
        this.requests = requests;
    }

    public getPendingRequests(): RequestListItemDto[] {
        return this.requests.filter((request: RequestListItemDto) => {
            return request.status.toLowerCase() == 'pending';
        });
    }

    private _search(requests: RequestListItemDto[], search: string) {
        if (search && search.length >= 3) {
            return requests.filter((request: RequestListItemDto) => {
                if(request.title.match(new RegExp(search, 'i'))) {
                    return true;
                } else if (request.issueNumber.match(new RegExp(search, 'i'))) {
                    return true;
                } else if (Utils.arrayContainsRegex(request.technologies, new RegExp(search, 'i'))) {
                    return true;
                } else {
                    return false;
                }
            });
        } else {
            return requests.filter(x => true);
        }
    }

    public getAllRequests(search: string = null, sortBy: string = null): RequestListItemDto[] {
        let requests = this._search(this.requests, search);
        return this._sort(requests, sortBy);
    }

    public filterByStatus(requestStatus: string, search: string = null, sortBy: string = null): RequestListItemDto[] {
        let requests = this._search(this.requests, search);
        let status = requestStatus.toLowerCase();

        requests = requests.filter((request: RequestListItemDto) => {
            return request.status.toLowerCase() == status;
        });

        return this._sort(requests, sortBy);
    }

    private _sort(requests: RequestListItemDto[], sortBy: string) {
        if (sortBy) {
            let sortFunction;
            if (sortBy.toLowerCase() == 'fnd') {
                // sort fndFunds.totalAmount from high to low
                sortFunction = (a: RequestListItemDto, b: RequestListItemDto) => {
                    if (a.fndFunds && b.fndFunds) {
                        return b.fndFunds.totalAmount - a.fndFunds.totalAmount;
                    } else if (a.fndFunds) {
                        return -1;
                    } else if (b.fndFunds) {
                        return 1;
                    }

                    return 0;
                };
            } else {
                sortFunction = (a: RequestListItemDto, b: RequestListItemDto) => {
                    if (a.hasOwnProperty(sortBy) && b.hasOwnProperty(sortBy)) {
                        let aSortBy = a[sortBy].toLowerCase();
                        let bSortBy = b[sortBy].toLowerCase();

                        if (aSortBy < bSortBy)
                            return -1;
                        if (aSortBy > bSortBy)
                            return 1;
                    } else if (a.hasOwnProperty(sortBy)) {
                        return 1;
                    } else if (b.hasOwnProperty(sortBy)) {
                        return -1;
                    }

                    return 0;
                };
            }
            return requests.sort(sortFunction);
        } else {
            return requests;
        }
    }
}