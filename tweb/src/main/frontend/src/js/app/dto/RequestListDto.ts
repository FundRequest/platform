import RequestListItemDto from './RequestListItemDto';

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

    public getAllRequests(search: string = null, sortBy: string = null): RequestListItemDto[] {
        let requests;

        if (search && search.length >= 3) {
            requests = this.requests.filter((request: RequestListItemDto) => {
                return request.title.match(new RegExp(search, 'i'));
            });
        } else {
            requests = this.requests.filter(request => true);
        }

        return this._sort(requests, sortBy);
    }

    public filterByStatus(requestStatus: string, search: string = null, sortBy: string = null): RequestListItemDto[] {
        let requests;
        let status = requestStatus.toLowerCase();
        if (search && search.length >= 3) {
            requests = this.requests.filter((request: RequestListItemDto) => {
                return request.status.toLowerCase() == status && request.title.match(new RegExp(search, 'i'));
            });
        } else {
            requests = this.requests.filter((request: RequestListItemDto) => {
                return request.status.toLowerCase() == status;
            });
        }

        return this._sort(requests, sortBy);
    }

    private _sort(requests: RequestListItemDto[], sortBy: string) {
        if (sortBy) {
            let sortFunction;
            if (sortBy.toLowerCase() == 'fnd') {
                // sort fndFunds.totalAmount from high to low
                sortFunction = (a: RequestListItemDto, b: RequestListItemDto) => {
                    if(a.fndFunds && b.fndFunds) {
                        return b.fndFunds.totalAmount - a.fndFunds.totalAmount
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