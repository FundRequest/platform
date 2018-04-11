class ControlButton {
    private readonly el: HTMLElement;
    private readonly buttonTexts: any;

    constructor(buttonEl, clickEvent) {
        this.el = buttonEl;
        if(this.el != null) {
            this.buttonTexts = JSON.parse((<any>this.el.dataset).wizardControlTexts);
            this.el.addEventListener('click', () => clickEvent());
        }
    }

    hide() {
        if (this.el != null) {
            this.el.classList.remove('disabled');
            this.el.classList.add('d-none');
        }
    }

    show() {
        if (this.el != null) {
            this.el.classList.remove('d-none', 'disabled');
        }
    }

    disable() {
        if (this.el != null) {
            this.el.classList.remove('d-none');
            this.el.classList.add('disabled');
        }
    }

    enable() {
        if (this.el != null) {
            this.el.classList.remove('d-none', 'disabled');
        }
    }

    updateText(stepNumber: number) {
        if (this.el != null) {
            if (this.buttonTexts.hasOwnProperty(stepNumber)) {
                this.el.innerHTML = this.buttonTexts[`${stepNumber}`];
            } else {
                this.el.innerHTML = this.buttonTexts['default'];
            }
        }
    }
}

class Step {
    public stepIcon: StepIcon;
    public panel: Panel;
    public number: number;
    public nextButton: ControlButton;
    public previousButton: ControlButton;
    public finishedCallback: any;

    private nextStep: Step = null;
    private previousStep: Step = null;

    constructor(stepIcon, panel, number, buttonNext, buttonPrevious, finishedCallback = null) {
        this.stepIcon = stepIcon;
        this.panel = panel;
        this.number = number;
        this.nextButton = buttonNext;
        this.previousButton = buttonPrevious;
        this.finishedCallback = finishedCallback;
    }

    public setNextStep(step: Step) {
        this.nextStep = step;
    }

    public setPreviousStep(step: Step) {
        this.previousStep = step;
    }

    public setActive() {
        if(this.previousStep != null) {
            this.previousStep.stepIcon.setCompleted();
            this.previousStep.panel.setBackwards();
        }
        this.stepIcon.unsetCompleted();
        this.panel.setActive();
        if(this.nextStep != null) {
            this.nextStep.stepIcon.unsetCompleted();
            this.nextStep.panel.setForwards();
        }
        this.updateButtons();
    }

    public finish() {
        this.finishedCallback();
    }

    public goToNext(): Step {
        if (this.nextStep != null) {
            this.nextStep.setActive();
            return this.nextStep;
        } else {
            this.finish();
            return this;
        }
    }

    public goToPrevious(): Step {
        if (this.previousStep != null) {
            this.previousStep.setActive();
            return this.previousStep;
        } else {
            return this;
        }
    }

    public updateButtons() {
        this.previousButton.updateText(this.number);
        this.nextButton.updateText(this.number);
        this.previousStep == null ? this.previousButton.hide() : this.previousButton.show();
    }
}

class StepIcon {
    private el: HTMLElement;

    constructor(stepEl) {
        this.el = stepEl;
    }

    public setCompleted() {
        this.el.classList.add('-completed');
    }

    public unsetCompleted() {
        this.el.classList.remove('-completed');
    }
}

class Panel {
    private el: HTMLElement;

    constructor(panelEl) {
        this.el = panelEl;
    }

    public setActive() {
        this.el.classList.remove('movingOutBackward', 'movingOutForward');
        this.el.classList.add('movingIn');
        this.el.parentElement.style.height = `${this.el.offsetHeight}px`;
    }

    public setBackwards() {
        this.el.classList.remove('movingIn', 'movingOutForward');
        this.el.classList.add('movingOutBackward');
    }

    public setForwards() {
        this.el.classList.remove('movingIn', 'movingOutBackward');
        this.el.classList.add('movingOutForward');

    }
}

class Steps {
    private el: HTMLElement;
    private buttonNext: ControlButton;
    private buttonPrevious: ControlButton;
    private steps: Step[];
    public currentStep: Step;
    public numberOfSteps: number;

    constructor(wizardEl: HTMLElement, numberOfSteps: number, buttonNext: HTMLElement, buttonPrevious: HTMLElement, finishedCallback) {
        this.el = wizardEl;
        this.numberOfSteps = numberOfSteps;
        this.initButtons(buttonNext, buttonPrevious);
        this.initSteps(finishedCallback);
    }

    private setCurrentStep(step: Step) {
        this.currentStep = step;
        this.currentStep.setActive();
    }

    private initSteps(finishedCallback) {
        this.steps = [];
        let previousStep = null;

        for (let i = 1; i <= this.numberOfSteps; i++) {
            let stepIcon: StepIcon = new StepIcon(this.el.querySelector(`[data-wizard-step="${i}"]`));
            let panel: Panel = new Panel(this.el.querySelector(`[data-wizard-panel="${i}"]`));
            let step: Step = new Step(stepIcon, panel, i, this.buttonNext, this.buttonPrevious, finishedCallback);

            if (previousStep != null) {
                previousStep.setNextStep(step);
            }

            step.setPreviousStep(previousStep);
            previousStep = step;
            this.steps.push(step);
        }

        this.setCurrentStep(this.steps[0]);
    }

    private initButtons(buttonNext, buttonPrevious) {
        this.buttonNext = new ControlButton(buttonNext, () => {this.goToNextStep(); });
        this.buttonPrevious = new ControlButton(buttonPrevious, () => {this.goToPreviousStep(); });
    }

    public goToNextStep() {
        this.currentStep = this.currentStep.goToNext();
    }

    public goToPreviousStep() {
        this.currentStep = this.currentStep.goToPrevious();
    }

    /*public goToStep(stepNumber: number) {
        this.steps.forEach((step) => {
            if (step.number < stepNumber) {
                step.stepIcon.setCompleted();
            } else {
                step.stepIcon.unsetCompleted();
            }
        })
    }*/
}

class Wizard {
    private readonly el: HTMLElement;
    private readonly buttonPrevious: HTMLElement;
    private readonly buttonNext: HTMLElement;

    private steps: Steps;

    constructor(wizardEl, finishedCallback) {
        this.el = wizardEl;
        this.buttonNext = this.el.querySelector('[data-wizard-control="next"]');
        this.buttonPrevious = this.el.querySelector('[data-wizard-control="previous"]');

        this.steps = new Steps(this.el, (<any>this.el.dataset).wizard, this.buttonNext, this.buttonPrevious, finishedCallback);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    Array.from(document.querySelectorAll('[data-wizard]')).forEach((wizardEl) => {
        new Wizard(wizardEl, () => alert('initate fund transaction'));
    });
});
