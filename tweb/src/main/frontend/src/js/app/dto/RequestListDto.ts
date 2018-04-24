import RequestDto from './RequestDto';
import {Utils} from '../Utils';

export default class RequestsListDto {
    private requests: RequestDto[] = [];

    constructor(requests: RequestDto[]) {
        this.requests = requests;
    }

    private _search(requests: RequestDto[], search: string) {
        if (search && search.length >= 3) {
            let regex = new RegExp(search, 'i');
            return requests.filter((request: RequestDto) => {
                if(request.title.match(regex)) {
                    return true;
                } else if(request.owner.match(regex)) {
                    return true;
                } else if (request.issueNumber.match(regex)) {
                    return true;
                } else if (Utils.arrayContainsRegex(request.technologies, regex)) {
                    return true;
                } else {
                    return false;
                }
            });
        } else {
            return requests.filter(x => true);
        }
    }

    public getAllRequests(search: string = null, sortBy: string = null): RequestDto[] {
        let requests = this._search(this.requests, search);
        return this._sort(requests, sortBy);
    }

    public getStarredRequests(search: string = null, sortBy: string = null): RequestDto[] {
        let requests = this._search(this.requests, search);
        requests = requests.filter((request: RequestDto) => {
            return request.starred;
        });
        return this._sort(requests, sortBy);
    }

    public filterByStatus(requestStatus: string, search: string = null, sortBy: string = null): RequestDto[] {
        let requests = this._search(this.requests, search);
        let status = requestStatus.toLowerCase();

        requests = requests.filter((request: RequestDto) => {
            return request.status.toLowerCase() == status;
        });

        return this._sort(requests, sortBy);
    }

    private _sort(requests: RequestDto[], sortBy: string) {
        if (sortBy) {
            let sortFunction;
            if (sortBy.toLowerCase() == 'fundings') {
                // sort fndFunds.totalAmount from high to low
                sortFunction = (a: RequestDto, b: RequestDto) => {
                    if (a.funds.usdFunds && a.funds.usdFunds) {
                        return b.funds.usdFunds - a.funds.usdFunds;
                    } else if (a.funds.usdFunds) {
                        return -1;
                    } else if (b.funds.usdFunds) {
                        return 1;
                    }

                    return 0;
                };
            } else {
                sortFunction = (a: RequestDto, b: RequestDto) => {
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