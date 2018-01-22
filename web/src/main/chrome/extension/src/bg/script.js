(function() {
    'use strict';

    let store = new Store("settings");
    let settings = {
        accountAddress: store.get('fndAccountAddress'),
        fundUrlPrefix: store.get('fndFundUrlPrefix') || "https://alpha.fundrequest.io/#/requests/fund?url=",
        optionsUrl: ""
    };

    function generateUUID() { // Public Domain/MIT
        let d = new Date().getTime();
        if (typeof performance !== 'undefined' && typeof performance.now === 'function') {
            d += performance.now(); //use high-precision timer if available
        }
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            let r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
    }

    chrome.management.getAll(function (extensionInfoList){
        for(let i=0; i < extensionInfoList.length && settings.optionsUrl.length <= 0; i++) {
            if(extensionInfoList[i].name === "FundRequest") {
                settings.optionsUrl = `chrome-extension://${extensionInfoList[i].id}/src/options_custom/index.html`;
            }
        }
    });

    chrome.extension.onMessage.addListener(
        function(request, sender, sendResponse) {
            let id = generateUUID();
            let currentTabId = sender.tab.id;

            switch (request.action) {
                case 'load':
                    chrome.pageAction.show(sender.tab.id);
                    sendResponse(settings);
                    break;
                case 'openOptions':
                    chrome.tabs.create({url: settings.optionsUrl}, function(tab) {
                        chrome.tabs.sendMessage(tab.id, {id: id}, function(response) {
                        });
                    });
                    break;
                case 'fund':
                    /***
                     * - Create a new tab with funding page
                     * -- Inject script to funding page
                     * -- Send message to new tab
                     * - When funding succeeded, close tab, add notification, reload funded button
                     * - When funding fails, close tab and add notification
                     */

                    chrome.tabs.create({url: settings.fundUrlPrefix + encodeURI(request.url)}, function(tab) {
                        chrome.tabs.executeScript(tab.id, {file: "src/inject/fnd/script.js"}, function() {
                            chrome.tabs.sendMessage(tab.id, {id: id}, function(response) {
                                response = typeof response !== 'undefined' ? response : {};
                                if (response.id === id) {
                                    if (chrome.runtime.lastError) {
                                        response.done = false;
                                        response.message = chrome.runtime.lastError;
                                    } else {
                                        response.done = true;
                                    }
                                    chrome.tabs.remove([tab.id]);
                                    chrome.tabs.update(currentTabId, {"active": true, "highlighted": true});

                                    sendResponse(response);
                                }
                            });
                        });
                    });
            }
            return true;
        }
    );
})();