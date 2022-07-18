package uimap;

import org.openqa.selenium.By;

/**
 * UI Map for Common Objects
 */
public class Common {

	public By textbox,readonlyTextbox,textarea,dialogTextbox,crossIcon;
	public By navigationbutton,button,dialogButton,unlockButton,headerButton;
	public By dropdown,menu,select,combobox,comboLookup,dialogComboListItem;
	public By mailframe,mailTextbox,readonlyTextarea,dropDownVal;
	public By tableLink,tableLink2,rateArticle;
	public By tab,links,SPlabel,mandatoryMark;
	public By checkbox,cardlabel,backendlabel,tablelinkText1;
	public By link,SPdropdownArrorWithoutName,anyLabelValue,sortByascending;
	public By datePicker,dialogTextBox,activities,warningicon;
	public By expandView,commoniframe,dialogDropDown,thirdfiltertextBox;
	public By surveyChoice,SP_Textarea,SP_Button,translateSearchLink,sparcgetdropdownvalue;
	public By readonlyFiled,addRemoveButton,groupmenberTextbox,escportaltab,escPoratlBtn,CommentsButton,TabButton,searchdropdown;
	public By dropDownSelect,dropDownClick,linkIcon,dialogTextArea,dialogBtn,linkText,dropDownSelectValue,search,multiSelect,arrowBtn;
	public By imgIcon,glideLookupSearch,lookupSearchBtn,notifyError,tableHeaderSearch,cardRows,readonlyDropdownValues;
	public By cardTableRows,cardDate,showEmailLink,hideEmailLink,preFilledText,previewIcon,tablelinkText,dialogRadioButton,textContentBodyFrame;
	public By textContentBody,comments,userComments,cardText,tableText,navbarTitle,dropdownWithoutName,tabText,tabNavButton,titleCard,calenderClick;
	public By textBoxInput,dialogComboBox,getNumItem,navLink,attachmentLink,breadcrumbLink,tableAttachLink,dialogCheckBox,comboTextBox;
	public By partialLink,multiplevaluedropdown,dropDownArrow,formdropdown,multivaldropdownWithName,formtextBox,formtextBox1t,formtextarea,formtextBox1;
	public By notificationMsg,dropdownWithoutName1,navigateButtonWithoutName,dropdownArrorWithoutName,tableColName,menuItem;
	public By dialogCard,sparctextbox,spselect,tabDropDown,tabTextBox,label,sortByDescending,dropDownSelectedValue,secondfilterdropdown,thirdfilterdropdown;
	public By textContentReadonly,comboboxreadonly,unlockButtonReadonly,validTo,pausetimer,spDropDownArrow,sparcDropDowntextbox;
	public By sptextContentBodyFrame,clickLabelSparc,spLabel,spDropDownButton,compareText,label1,spDropDownButtonVal,editabledropdown,readonlyTextContent;
	public Common(String strLabel) {
		
		//textbox=By.xpath("//*[text()='"+strLabel+"']//ancestor-or-self::*[contains(@id,'element') or contains(@class,'search')]//following-sibling::div[contains(@class,'input') or @style]//input[(@type='search' or @type='text' or contains(@id,'text')) and not(@type='hidden')]");
	//textbox=By.xpath("//*[text()='"+strLabel+"']//ancestor-or-self::*[contains(@id,'element') or contains(@class,'search')]//descendant::div[contains(@class,'input') or @style]//input[(@type='search' or @type='text' or contains(@id,'text')) and not(@type='hidden')]");
		//textbox=By.xpath("//*[text()='"+strLabel+"' and (contains(@class,'label-text') or contains(@class,'list_search_title '))]//ancestor-or-self::*[contains(@id,'element') or (contains(@id,'search'))]//descendant::div[contains(@class,'input') or @style]//input[not(@type='hidden')]");	
		//siva 11/16
		//Siva
		//textbox = By.xpath("//*[text()='"+strLabel+"' and (contains(@class,'label-text') or contains(@class,'list_search_title '))]//ancestor-or-self::*[contains(@id,'element') or (contains(@id,'search'))]//descendant::div[contains(@class,'input') or @style]//input[not(@type='hidden')] |  .//*[text()='"+strLabel+"']//parent::div//input[not(contains(@class,'readonly'))]");
	//Yash
		textbox = By.xpath("//*[text()=\""+strLabel+"\" and (contains(@class,'label-text') or contains(@class,'list_search_title '))]//ancestor-or-self::*[contains(@id,'element') or (contains(@id,'search'))]//descendant::div[contains(@class,'input') or @style]//input[not(@type='hidden')] | .//*[text()=\""+strLabel+"\"]//parent::div//input[not(contains(@class,'readonly'))]");
		sparctextbox=By.xpath(".//*[text()='"+strLabel+"']//ancestor::div[contains(@class,'form-group')]//input[not(@type='hidden')]");
		sparcgetdropdownvalue = By.xpath(".//*[text()='"+strLabel+"']//ancestor::div[contains(@class,'form-group') or contains(@class,'select2-search')]//span[contains(@id,'chosen')]");
		textarea=By.xpath("//*[text()='"+strLabel+"']//ancestor-or-self::*[contains(@id,'input') and @aria-hidden='false' or contains(@id,'label') and @data-type='label' or contains(@data-type,'input')and not(contains(@style,'display:none;'))]//following-sibling::*[contains(@class,'input') or contains(@class,'field')]//textarea");
		navigationbutton = By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::a[@role='button']");
		searchdropdown=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@class,'modal-header') or contains(@class,'section_header')]//following-sibling::*[contains(@class,'modal-body') or contains(@class,'group')]//descendant::*[contains(@class,'select') and (contains(@id,'auto') or contains(@class,'input-select') and contains(@id,'search_input'))]//following::a[contains(@class,'select')]");
		dropdown=By.xpath("//*[text()='" + strLabel + "']//ancestor::div[@type='choice']//following-sibling::div//select");
		multiplevaluedropdown =By.xpath("//*[text()='" + strLabel + "']//following-sibling::div//select");
		//siva 04/01
		//spDropDownArrow = By.xpath("//*[text()='" + strLabel + "']//ancestor::div[contains(@class,'form-group')]//a");
		//Aparna 06-Jan-2022
		spDropDownArrow = By.xpath("(//*[text()='" + strLabel + "']//ancestor::div[contains(@class,'form-group')]//a)|(.//*[text()='"+strLabel+"']//following::*[contains(@class,'select2')])");
		sparcDropDowntextbox=By.xpath(".//*[text()='"+strLabel+"']//ancestor::div[contains(@class,'form-group') or contains(@class,'select2-search')]//input[not(@type='hidden') and not(@aria-required='true')]");
		//button=By.xpath("//*[contains(@class,'section_header_content')]//descendant::button[text()='" + strLabel + "' and contains(@class,'btn') and not(contains(@class,'header'))]");
		//button=By.xpath("//*[(contains(@class,'tab_section') and @aria-hidden='false') or contains(@class,'action_button') or contains(@class,'container') and contains(@class,'section_header_content') or contains(@class,'panel')]//descendant::button[text()='" + strLabel + "' and (not(contains(@class,'header')) and @type='submit' or contains(@class,'btn') and not(contains(@data-dismiss,'modal')))]");
		//button=By.xpath("(//*[contains(@class,'navbar') and (@role='navigation' or @role='toolbar')]//descendant::*[local-name()='button' or local-name()='a'][text()='" + strLabel + "'])|(//*[text()='" + strLabel + "']//ancestor-or-self::a[not(contains(@class,'link'))])|(//*[(contains(@class,'tab_section') and @aria-hidden='false')]//descendant::button[contains(text(),'" + strLabel + "') and @type='submit'])|(//button[@id='" + strLabel + "' or text()='" + strLabel + "' and contains(@class,'activity')])");
		//Comments: Button xpath changed for Comments POST Button
		multivaldropdownWithName =By.xpath("//*[text()='" + strLabel + "']//following-sibling::select");
		formtextBox  = By.xpath(".//*[text()='"+strLabel+"']//ancestor::div[contains(@class,'form-group') or contains(@class,'slushbucket')]//input[contains(@class,'form-control')]");
		formtextBox1  = By.xpath(".//*[text()='"+strLabel+"']//ancestor::tr[@class='filter_row']//input[contains(@class,'form-control')  and not(@type='hidden')]");
		formtextarea= By.xpath(".//span[text()='"+strLabel+"']//ancestor::tr[@class='filter_row']//textarea");
		//formdropdown= By.xpath(".//span[text()='"+strLabel+"']//ancestor::tr[@class='filter_row']//td[@t='"+strLabel+"' or @data-value='"+strLabel+"']//select");
		//Amit 01/03 siva 04/01
		formdropdown= By.xpath("(.//span[text()=\""+strLabel+"\"]//ancestor::tr[@class='filter_row']//td[@t=\""+strLabel+"\" or @data-value=\""+strLabel+"\"]//select)|(.//*[text()='"+strLabel+"']//ancestor::table[contains(@class,'required')]//select)");
		//notificationMsg = By.xpath("(.//*[contains(@class,'notification')]//*[text()='"+strLabel+"'])|(.//*[contains(@class,'notification') and text()='"+strLabel+"'])");
		//Aparna 10/02
		notificationMsg = By.xpath("(.//*[contains(@class,'notification')]//*[text()=\""+strLabel+"\"])|(.//*[contains(@class,'notification') and text()=\""+strLabel+"\"])|(.//*[@class='outputmsg_text' and contains(text(),\""+strLabel+"\")])");
		//button=By.xpath("(//*[contains(@class,'navbar') and (@role='navigation' or @role='toolbar' or contains(@class,'navbar'))]//descendant::*[local-name()='button' or local-name()='a'][text()='"+strLabel+"'])|(//*[text()='"+strLabel+"']//ancestor-or-self::a[not(contains(@class,'link'))])|(//button[@id='"+strLabel+"' or text()='"+strLabel+"' and contains(@class,'activity')])");
		//button=By.xpath("(//*[contains(@class,'navbar') and (@role='navigation' or @role='toolbar' or contains(@class,'navbar'))]//descendant::*[local-name()='button' or local-name()='a'][text()='"+strLabel+"'])|(//*[text()='"+strLabel+"']//ancestor-or-self::a[not(contains(@class,'link'))])|(//button[@id='"+strLabel+"' or text()='"+strLabel+"' and contains(@class,'activity')])|(//button[contains(text(),'"+strLabel+"')])|(.//*[@role='button' and contains(@aria-label,'"+strLabel+"')])|(.//*[@type='button' and contains(@aria-label,'"+strLabel+"')])");
		button=By.xpath("(//*[contains(@class,'navbar') and (@role='navigation' or @role='toolbar' or contains(@class,'navbar'))]//descendant::*[local-name()='button' or local-name()='a'][text()='"+strLabel+"'])|(//*[text()='"+strLabel+"']//ancestor-or-self::a[not(contains(@class,'link'))])|(//button[@id='"+strLabel+"' or text()='"+strLabel+"' and contains(@class,'activity')])|(//button[contains(text(),'"+strLabel+"')])|(.//*[@role='button' and contains(@aria-label,'"+strLabel+"')])|(.//*[@type='button' and contains(@aria-label,'"+strLabel+"')])");
		dropdownWithoutName1 = By.xpath("//*[contains(text(),'"+strLabel+"')]//parent::select");
		//navigateButtonWithoutName  = By.xpath("(.//*[@role='button' and contains(@aria-label,'"+strLabel+"')])|(.//*[@id='addRemoveButtons']//*[text()='"+strLabel+"'])");
		//dropdownArrorWithoutName=By.xpath("//*[contains(text(),'"+strLabel+"')]//ancestor::div[@title='Choose option' or @data-original-title='Choose option']//span[contains(@class,'arrow')]");
		//Yash 03-01-2022
		//navigateButtonWithoutName  = By.xpath(".//*[@role='button' and contains(@aria-label,'"+strLabel+"')]|//*[@id='addRemoveButtons']//a[contains(@class,'add')]");
		navigateButtonWithoutName = By.xpath("(.//*[@role='button' and contains(@aria-label,'"+strLabel+"')])|(.//*[@id='addRemoveButtons']//*[text()='"+strLabel+"'])");
		dropdownArrorWithoutName=By.xpath("//*[contains(text(),'"+strLabel+"')]//ancestor::div[@title='Choose option' or @data-original-title='Choose option']//span[contains(@class,'arrow')]");
		tableColName = By.xpath(".//*[contains(@class,'col-header') and text()='" + strLabel + "' and @tabindex='0']");
		menuItem =By.xpath(".//div[@role='menu' and @gsft_has_scroll='true']//following::div[text()='" + strLabel + "' and @role='button']");
		//Amit 11/01
		//CommentsButton=By.xpath("//button[text()='"+strLabel+"'][1]|(//*[text()='"+strLabel+"']/ancestor-or-self::*[@role='button'])[last()]");
		//Yash
	//	CommentsButton=By.xpath("//button[normalize-space(text())='"+strLabel+"'][1]|(//*[text()='"+strLabel+"']/ancestor-or-self::*[@role='button'])[last()]");
		CommentsButton=By.xpath("//button[normalize-space(text())='"+strLabel+"'][1]|(//*[normalize-space(text())='"+strLabel+"']/ancestor-or-self::*[@role='button'])[last()]");
		//CommentsButton=By.xpath("//button[text()='"+strLabel+"'][1]");
		//siva 11/17 modified for CM Standard TC
		TabButton=By.xpath("(//*[(contains(@class,'tab_section') and @aria-hidden='false')]//descendant::button[contains(text(),'"+strLabel+"') and @type='submit'])|(//*[(contains(@class,'tab_section') or contains(@class,'tabs2_list')) and @aria-hidden='false']//button[text()='"+strLabel+"'])");
		//TabButton=By.xpath("(//*[(contains(@class,'tab_section') and @aria-hidden='false')]//descendant::button[contains(text(),'"+strLabel+"') and @type='submit'])|(//*[(contains(@class,'nav_top_border'))]//descendant::button[contains(text(),'"+strLabel+"') and @type='submit'])");
		//TabButton=By.xpath("(//*[(contains(@class,'tab_section') and @aria-hidden='false')]//descendant::button[contains(text(),'"+strLabel+"') and @type='submit' or @type='button'])[1])");
		//headerButton=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[local-name()='a' or local-name()='button']");
		checkbox=By.xpath("(//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@class,'form-group')]//descendant::input[@type='checkbox'])|(//*[contains(@data-original-title,'"+strLabel+"')]//ancestor-or-self::*[contains(@class,'check') and contains(@class,'item')]//descendant::input[@type='checkbox'][1])");
		//menu=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@class,'select') and @role='listbox']//descendant::li");
		
		menu=By.xpath("(//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@class,'select') and @role='listbox']//descendant::li)|(//*[text()='" + strLabel + "' and @role='menuitem'])|(//*[text()='" + strLabel + "']//ancestor-or-self::a[@role='menuitem'])|(//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@class,'select') and contains(@aria-label,'Choose option')])");
		//Amit 
		//crossIcon=By.xpath("//*[contains(@id,'"+ strLabel + "') and @role='button']");
		warningicon = By.xpath("//input[@value=\""+strLabel+"\"]//ancestor::div//span[contains(@class,'icon-warning')]");

		tableLink=By.xpath(".//descendant::*[local-name()='th' or local-name()='td']["+ strLabel + "]//a");
		tableLink2=By.xpath(".//descendant::*[local-name()='th' or local-name()='td']['" + strLabel + "']//preceding-sibling::td//a[contains(@class,'link')]");
		readonlyTextbox=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@id,'element')]//descendant::*[local-name()='input' or local-name()='select'][not(@type='hidden')][not(@onchange)]");
		//readonlyFiled=By.xpath("//*[text()='" + strLabel + "' and contains(@class,'label')]//ancestor-or-self::*[contains(@id,'element')]//descendant::*[contains(@class,'input_controls')]//descendant::*[not(@type='hidden') and @readonly  or contains(@id,'readonly') or contains(@class,'disable')]");
		readonlyFiled=By.xpath("//*[text()='" + strLabel + "' and contains(@class,'label')]//ancestor-or-self::*[contains(@id,'element')]//descendant::*[contains(@class,'input_controls')]//descendant::*[local-name()='input' or local-name()='select' or local-name()='textarea'][not(@type='hidden') and (@readonly  or contains(@id,'readonly') or contains(@class,'disabled'))]//option[@selected]");
		//Amit
		comboLookup=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@id,'element')]//descendant::button[contains(@id,'lookup')] | //*[text()='" + strLabel +"']/parent::span[contains(@id,'search')]//input");
		
		//comboLookup=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@id,'element')]//descendant::button[contains(@id,'lookup')]");
		dialogComboListItem=By.xpath("//*[contains(@id,'dialog') and @role='dialog']//descendant::*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@class,'section') and contains(@id,'dialog')]//descendant::*[contains(@class,'list-item')]");
		//yash 05-01-2022
		//SPlabel = By.xpath("//*[contains(text(),'"+strLabel+"')]");
		
		//Yash 05-01-2022
		SPdropdownArrorWithoutName = By.xpath(".//*[contains(text(),'"+strLabel+"')]//ancestor::div[@title='Choose option' or @data-original-title='Choose option'or contains(@id,'autogen')]//span[text()='"+strLabel+"']");
		//Amit
		//select=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@id,'element') or contains(@class,'select')]//descendant::select");
		select=By.xpath("(//*[text()=\"" + strLabel + "\"]//ancestor-or-self::*[contains(@id,'element') or contains(@class,'select')]//descendant::select)|(//*[text()=\"" + strLabel + "\"]//parent::*[contains(@class,'col')]//descendant::select)|(//select[@aria-label=\"" + strLabel + "\" and contains(@id,'select')])|(//*[text()=\"" + strLabel + "\"]//parent::div//select)");
		//select=By.xpath("(//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@id,'element') or contains(@class,'select')]//descendant::select)|(//*[text()='" + strLabel + "']//parent::*[contains(@class,'col')]//descendant::select)|(//select[@aria-label='" + strLabel + "' and contains(@id,'select')])|(//*[text()='" + strLabel + "']//parent::div//select)");
		//siva 05/01
		spselect=By.xpath(".//*[text()='" + strLabel + "']//parent::div[contains(@class,'select-container')]//span[contains(@class,'sort')]");
		unlockButton=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[contains(@id,'element')]//descendant::button[contains(@id,'unlock')]");
		//tab=By.xpath("//span[contains(@class,'tab') and contains(translate(text(),'\u00A0', ' '),'" + strLabel + "')]");
		//tab=By.xpath("(//*[contains(@class,'tab') and contains(translate(text(),'\u00A0', ' '),'" + strLabel + "')]//ancestor::*[@role='tab'])");
		//siva 12/16- Security incident module
		tab=By.xpath("(//*[contains(@class,'tab') and starts-with(translate(text(),'\u00A0', ' '),'" + strLabel + "')]//ancestor::*[@role='tab'])");
		//tab=By.xpath("(//*[contains(@class,'tab') and contains(text(),'" + strLabel + "')]//ancestor::*[@role='tab'])");
		//tab=By.xpath(".//div[@role='tablist']/span[@class='tab_header']/span[@role='tab']/span[contains(@class,'tab') and contains(text(),'" + strLabel + "')]");
		//
		mailframe=By.xpath("//iframe[@title='"+strLabel+"']");
		mailTextbox=By.xpath("//*[@aria-label='"+strLabel+"' and contains(@class,'content')]");
		link=By.xpath("(//*[text()='"+strLabel+"' and contains(@class,'link')]//ancestor-or-self::a)|(//*[text()='"+strLabel+"' and not(@role) or @title='"+strLabel+"']//ancestor-or-self::a)|(//a[text()='"+strLabel+"'])|(//b[text()='"+strLabel+"'])|(.//*[@data-original-title='"+strLabel+"']//parent::a)");
		datePicker=By.xpath("//*[text()='"+strLabel+"']//ancestor-or-self::*[contains(@id,'element')]//following-sibling::div[contains(@class,'input')]//button[contains(@class,'date_time')]");
		dialogTextbox=By.xpath("//*[normalize-space(text())='"+strLabel+"']//ancestor-or-self::*[contains(@id,'dialog') and @role='dialog' or contains(@class,'dialog')]//descendant::input[@type='text']");
		expandView=By.xpath("//*[text()='"+strLabel+"']//ancestor-or-self::*[contains(@class,'tree')]//preceding-sibling::td//img[contains(@class,'tree')]");
		SP_Button=By.xpath("//*[(text()='" + strLabel + "' or @value='" + strLabel + "' and @role='button')][1]//ancestor-or-self::*[local-name()='button' or local-name()='span' and contains(@class,'input')]");
		SP_Textarea=By.xpath("//*[text()='"+strLabel+"']//ancestor-or-self::*[contains(@class,'label') or contains(@class,'panel-heading')]//following-sibling::*[contains(@class,'panel') or contains(@class,'scope')]//textarea[@role='textbox']");
		translateSearchLink=By.xpath("//*[normalize-space(text())='"+strLabel+"']");
		addRemoveButton=By.xpath("//a[contains(@aria-label,'"+strLabel+"') and @type='button']");
		groupmenberTextbox=By.xpath("//*[text()='" + strLabel + "']//ancestor-or-self::*[@class='row']//following::input[not(@type='hidden') and @type='search']");
		escportaltab=By.xpath("//a[text()='"+strLabel+"' and @role='tab']");
		escPoratlBtn=By.xpath("//button[normalize-space(text())='"+strLabel+"' and @type='button']");
		dropDownArrow = By.xpath("//label[text()='"+strLabel+"']//ancestor::div[contains(@class,'form-group')]//div//span[contains(@class,'arrow')]");
		dropDownClick = By.xpath("//*[text()='"+strLabel+"']//parent::div//descendant::div//button[contains(@class,'dropdown')]");
		dropDownSelectValue = By.xpath("//*[text()='"+strLabel+"']//parent::div[@role='option']");
		tabDropDown = By.xpath(".//*[text()='"+strLabel+"']//ancestor::div[contains(@class,'nav_top_border') and contains(@id,'approval')]//select");
		tabTextBox = By.xpath(".//*[text()='"+strLabel+"']//ancestor::div[contains(@class,'nav_top_border') and contains(@id,'approval')]//input[contains(@name,'text')]");
		linkIcon = By.xpath("//*[text()='"+strLabel+"']//ancestor::div[@class='iconlink']");
		//Yash 07-01-2022
		dialogTextArea = By.xpath("(//label[text()='"+strLabel+"']//ancestor::div[contains(@class,'form-group')]//textarea)|(//*[normalize-space(text())='"+strLabel+"']//ancestor::table//td/textarea[contains(@id,'dialog')])|(.//*[text()='"+strLabel+"']//ancestor::div//textarea)");
		//dialogTextArea = By.xpath("(//label[text()='"+strLabel+"']//ancestor::div[contains(@class,'form-group')]//textarea)|(//*[normalize-space(text())='"+strLabel+"']//ancestor::table//td/textarea[contains(@id,'dialog')])");
		//dialogTextArea=By.xpath("//*[normalize-space(text())='"+strLabel+"']//ancestor::table//td/textarea[contains(@id,'dialog')]");
		//dialogTextArea = By.xpath("//label[text()='"+strLabel+"']//ancestor::div[contains(@class,'form-group')]//textarea");
		dialogBtn = By.xpath("//*[text()='"+strLabel+"']//ancestor::div[contains(@class,'sparc-order-bottom') or contains(@class,'footer')]//parent::div[@role]");
		linkText = By.xpath("//*[text()='"+strLabel+"']//parent::a");
		search = By.xpath("//label[text()='"+strLabel+"']//ancestor::div[@class='row']//following-sibling::div[contains(@class,'row')]//descendant::input[@type='search']");
		multiSelect = By.xpath("//label[text()='"+strLabel+"']//ancestor::div[@class='row']//following-sibling::div[contains(@class,'row')]//descendant::select[@aria-label='"+strLabel+"']");
		lookupSearchBtn = By.xpath("//*[text()='"+strLabel+"']//ancestor::div[@type='reference']//following-sibling::div//button[contains(@id,'lookup')]");
		notifyError = By.xpath("//*[text()='"+strLabel+"']//ancestor::div[contains(@id,'state')]//following-sibling::div[contains(@class,'notification-error')]");
		dropdown= By.xpath("//*[text()='"+strLabel+"']//ancestor::div[contains(@class,'header')]//following-sibling::div[contains(@class,'container')]//a");
		tableHeaderSearch = By.xpath("//*[text()='"+strLabel+"']//ancestor::div[@role='toolbar']//following-sibling::div//table[contains(@id,'table') or contains(@id,'report') and not(contains(@id,'clone'))]//thead//tr//td[3]//input");
		readonlyDropdownValues = By.xpath("//*[text()='"+strLabel+"' and contains(@class,'label')]//ancestor-or-self::*[contains(@id,'element')]//descendant::*[contains(@class,'input_controls')]//descendant::*[local-name()='input' or local-name()='select' or local-name()='textarea'][not(@type='hidden') and (@readonly  or contains(@id,'readonly') or contains(@class,'disabled'))]//option");
		partialLink  = By.xpath(".//div[contains(@class,'outputmsg')]//a[contains(text(),'"+strLabel+"')]");
		cardRows = By.xpath("//*[contains(text(),'"+strLabel+"')]//ancestor::li//div[contains(@class,'card-component_first')]//following-sibling::div[contains(@class,'records')]//ul//li");
		cardTableRows = By.xpath("//*[text()='"+strLabel+"']//ancestor::table[@class='table-wrapper']//following-sibling::table[@class='body_Class']//descendant::table[@align='left'][1]//tr");
		cardDate = By.xpath("//*[contains(text(),'"+strLabel+"')]//ancestor::li//div[contains(@class,'card-component_first')]//span[contains(@class,'time')]//div[contains(@class,'date-calendar')]");
		showEmailLink = By.xpath("//*[contains(text(),'"+strLabel+"')]//ancestor::li//div[contains(@class,'card-component_first')]//following-sibling::div[contains(@class,'records')]//ul//li//a[@show-label]");
		hideEmailLink = By.xpath("//*[contains(text(),'"+strLabel+"')]//ancestor::li//div[contains(@class,'card-component_first')]//following-sibling::div[contains(@class,'records')]//ul//li//a[@action-type='hide-email']");
		preFilledText = By.xpath("//*[text()='"+strLabel+"' and (contains(@class,'label-text') or contains(@class,'list_search_title '))]//ancestor-or-self::*[contains(@id,'element') or (contains(@id,'search'))]//descendant::div[contains(@class,'input') or @style]//p");
		previewIcon = By.xpath("//*[text()='"+strLabel+"']//ancestor-or-self::*[contains(@id,'element')]//descendant::button[contains(@id,'viewr')]");
		tablelinkText = By.xpath("//*[text()='"+strLabel+"']//ancestor::tr/td/a[not(@class='linked') and not(@role='button')]");
		tablelinkText1 = By.xpath(".//*[contains(text(),'"+strLabel+"')]//ancestor::tr/td/a[not(@class='linked') and not(@role='button')]");
		textContentBodyFrame = By.xpath("//iframe[@title='"+strLabel+"']");
		
		textContentBody = By.xpath("//*[@aria-label='"+strLabel+"' and contains(@class,'content-body')]");
		userComments = By.xpath("//span[text()='"+strLabel+"']//ancestor::li[contains(@class,'card_comments')]//div[contains(@class,'summary')]//span");
		cardText = By.xpath("//*[text()='"+strLabel+"']//ancestor::table[@class='table-wrapper']//following-sibling::table[@class='body_Class']//descendant::table[@align='left'][1]//tr[9]//td");
		tableText = By.xpath("//*[text()='"+strLabel+"']//parent::tr/td[5]");
		navbarTitle = By.xpath("//*[text()='"+strLabel+"']//parent::a[@role='button']");
		dropdownWithoutName = By.xpath("//*[text()='"+strLabel+"' and not(@value='closed_at')]//parent::select");
		tabText = By.xpath(".//span[@class='tab_header']//span[contains(text(),'"+strLabel+"')]");
		tabNavButton = By.xpath("//*[text()='"+strLabel+"']//ancestor::div[@class='navbar-header']//button[@type='submit']");
		//siva 04/01
		titleCard = By.xpath("(//div[contains(@class,'rec-card')]//div[contains(@class,'title')][contains(text(),'"+strLabel+"')])|(//div[@class='search-card-inner-container']//*[contains(text(),'"+strLabel+"')])");
		calenderClick = By.xpath("//button[@aria-label='Show calendar']");
		textBoxInput = By.xpath("//label[text()='"+strLabel+"']//parent::div//span//input[@type='text' or @type='string']");
		dialogComboBox = By.xpath("//label[text()='"+strLabel+"']//parent::div//span//input[@role='combobox']");
		getNumItem = By.xpath("//*[text()='"+strLabel+"']//ancestor::thead//following-sibling::tbody//tr//td[contains(@ng-if,'number')]//a[@title]");
		//navLink = By.xpath("//div[@role='navigation']//ul//li//a[text()='"+strLabel+"']");
		//aparna 10/01
		navLink = By.xpath("//div[@role='navigation'or contains(@class,'nav')]//ul//li//a[text()='"+strLabel+"']");
		attachmentLink = By.xpath("//a[contains(text(),'"+strLabel+"')]//ancestor::li//following-sibling::li//a[@class='content_editable']");
		breadcrumbLink = By.xpath("//*[text()='"+strLabel+"']//parent::a");
		tableAttachLink = By.xpath("//*[text()='"+strLabel+"']//ancestor::tr//td//a[@class='linked formlink']");
		dialogCheckBox = By.xpath("//table[not(@class)]//td[text()='"+strLabel+"']//parent::tr//input[@type='checkbox']");
		comboTextBox = By.xpath("//*[text()='"+strLabel+"' and (contains(@class,'label-text') or contains(@class,'list_search_title '))]//ancestor-or-self::*[contains(@id,'element') or (contains(@id,'search'))]//descendant::div[contains(@class,'input') or @style]//input[not(@type='hidden') and @role]");
		dialogCard = By.xpath("//*[text()='"+strLabel+"']//ancestor::div[@role='dialog']");
		//siva 01/02
		
		//backendlabel = By.xpath("//*[text()='" + strLabel + "']");
		//yash 10/01
		backendlabel = By.xpath("//*[text()=\""+strLabel+"\"]");
		label1 = By.xpath("//*[contains(text(),'" + strLabel + "')]");
		sortByDescending = By.xpath("(.//*[text()='" + strLabel + "' and contains(@data-original-title,'ascending')])|(.//th[@sort_dir='none']//*[text()='" + strLabel + "'])");
		sortByascending = By.xpath("(.//*[text()='" + strLabel + "' and contains(@data-original-title,'descending')])|(.//th[@sort_dir='none']//*[text()='" + strLabel + "'])");
		dropDownSelectedValue = By.xpath(".//*[text()='Selected']//parent::div//select//option[text()='" + strLabel + "']");
		secondfilterdropdown = By.xpath("//*[contains(@aria-label,'"+strLabel+"')]//parent::select");
		thirdfilterdropdown = By.xpath("(//*[contains(@aria-label,'"+strLabel+"')]//parent::select)[last()]");
		//siva 10/01
		thirdfiltertextBox = By.xpath("(//input[contains(@aria-label,'"+strLabel+"') and contains(@class,'filerTableInput')])[last()]");
		
		
		//textContentReadonly = By.xpath("//body[@aria-label='"+strLabel+"']/p");
		textContentReadonly = By.xpath("(//body[@aria-label='"+strLabel+"']/p)|(//*[text()='"+strLabel+"']//ancestor-or-self::div[@class='row']//span/p)");
		//Jayausurya
		readonlyTextContent = By.xpath("//*[text()='" + strLabel + "']//ancestor::div[contains(@class,'col')]//*[contains(@id,'readonly')]/p");
		activities = By.xpath("//*[contains(text(),'" + strLabel+ "')]//parent::div//following-sibling::div//span[contains(@class,'body')]");
		//Aparna 30-Dec-2021
		comboboxreadonly= By.xpath("(//*[text()='"+ strLabel   +"']//ancestor-or-self::*[contains(@id,'element')]//descendant::input[@readonly='readonly'])[last()]");

		//Aparna 31-Dec-2021
		unlockButtonReadonly = By.xpath("(//*[text()='" +strLabel +"']//ancestor-or-self::*[contains(@id,'element')]//descendant::button[contains(@id,'unlock')]//following::p)");

		//Aparna 29-Dec-2021
		validTo = By.xpath("//*[text()='"+strLabel+"']//ancestor-or-self::*[contains(@id,'element')]//following-sibling::div[contains(@class,'input')]//a");
//Supriya 02/01
		pausetimer =By.xpath("//*[text()='" + strLabel + "']//parent::label//following::a/span[2]");
		/*pausetimer =By.xpath("//div[@class='search-card-inner-container']//*[contains(text(),'"+strLabel+"')]");*/
		//siva 04/01
		sptextContentBodyFrame = By.xpath(".//*[text()='" + strLabel + "']//ancestor::div[contains(@class,'form-group')]//iframe");
		//Supriya 04/01
		clickLabelSparc= By.xpath("//div[@class='search-card-inner-container']//*[contains(text(),'"+strLabel+"')]");
		//Siva 05/01
		//spLabel = By.xpath("(//*[contains(text(),'" + strLabel + "')  and contains(@class,'label')])|(//*[contains(text(),'" + strLabel + "')  and contains(@class,'binding')])|(//*[contains(text(),'" + strLabel + "')  and contains(@class,'scope')])");
		//Aparna 06/01
		//spLabel = By.xpath("(//*[contains(text(),'"+strLabel+"') and contains(@class,'label')])|(//*[contains(text(),'"+strLabel+"') and contains(@class,'binding')])|(//*[contains(text(),'"+strLabel+"') and contains(@class,'scope')])|(//*[contains(text(),'"+strLabel+"') and contains(@class,'pad')])|(//*[contains(text(),'"+strLabel+"') and contains(@class,'header')])|(//*[contains(text(),'"+strLabel+"') and contains(@role,'button')])");
	//Amit 18/01
		spLabel = By.xpath("(//*[contains(text(),'" + strLabel + "') and contains(@class,'label')])|(//*[contains(text(),'"
                + strLabel + "') and contains(@class,'binding')])|(//*[contains(text(),'" + strLabel
                + "') and contains(@class,'scope')])|(//*[contains(text(),'" + strLabel
                + "') and contains(@class,'pad')])|(//*[contains(text(),'" + strLabel
                + "') and contains(@class,'header')])|(//*[contains(text(),'" + strLabel
                + "') and contains(@role,'button')])| (//*[normalize-space(text())='" + strLabel
                + "' and contains(@class,'heading')])| //div[contains(@class,'empty-tour')]");

		//Amit 05-Jan-2022
		compareText = By.xpath("//*[text()='"+ strLabel + "' and not(@role) or @title='"+ strLabel + "']");
		editabledropdown=By.xpath("//*[starts-with(text(),'"+strLabel+"')]/ancestor::div[1]/following-sibling::div//*[@selected='SELECTED']");
		links=By.xpath("//"+strLabel+"//descendant::a");

		 commoniframe= By.xpath("//iframe[contains(@class,'iframe') or @id='"+strLabel+"']");
		 cardlabel=By.xpath("//*[contains(@class,'card') and text()='"+strLabel+"']");
		 label=By.xpath("//*[(contains(@class,'card') or @class='context_item' or contains(@class,'sparc')) and text()='"+strLabel+"']");
		 readonlyTextarea=By.xpath("//*[text()='" + strLabel + "' and contains(@class,'label')]//ancestor-or-self::*[contains(@id,'element')]//descendant::*[contains(@class,'input_controls')]//descendant::*[local-name()='input' or local-name()='select' or local-name()='textarea'][not(@type='hidden') and (@readonly  or contains(@id,'readonly') or contains(@class,'disabled'))]");
		
		 anyLabelValue=By.xpath("//*[text()='" + strLabel + "']/following-sibling::td/span | //*[normalize-space(text())='" + strLabel + "']/following::*[not(contains(@id,'arrow'))][1] | //*[normalize-space(text())='" + strLabel + "']/preceding-sibling::*[1]");
		 rateArticle=By.xpath(".//*[text()='"+ strLabel + "']//parent::div//following::span//i[@title='5']");
		// mandatoryMark =  By.xpath(".//*[text()='"+ strLabel + "']//ancestor::div[contains(@class,'form-group')]//*[contains(@class,'mandatory required')]");
//		 mandatoryMark = By.xpath("(.//*[text()='" + strLabel
//				 + "']//ancestor::div[contains(@class,'form-group')]//*[contains(@aria-label,'Mandatory')])|(.//*[text()='"+ strLabel + "']//ancestor::div[contains(@class,'form-group')]//*[contains(@class,'mandatory required')])");
		 /*mandatoryMark = By.xpath(".//*[text()='" + strLabel
				 + "']//ancestor::div[contains(@class,'form-group')]//*[contains(@aria-label,'Mandatory') or contains(@class,'mandatory required')]");*/
		 //Aparna 19/01
		 mandatoryMark = By.xpath(".//*[text()='" + strLabel + "']//ancestor::div[contains(@class,'form-group')]//*[contains(@aria-label,'Mandatory') or contains(@class,'mandatory required') or (@mandatory='true')]");
		 crossIcon = By.xpath("//*[contains(@id,'" + strLabel + "') and contains(@class,'icon')]");
		 dropDownVal = By.xpath(" (.//div[contains(@class,'select')]//*[text()='" + strLabel + "'])[last()]");
		
	}

	public Common(String strLabel1, String strLabel2) {
		navigationbutton = By.xpath("//*[text()='" + strLabel1 + "']//ancestor-or-self::a[@role='button']"
				+ "//ancestor::li//descendant::*[text()='" + strLabel2 + "']//ancestor-or-self::a");
		combobox=By.xpath("//*[text()='"+strLabel1+"']//ancestor-or-self::*[contains(@class,'row') and contains(@class,'section')]//descendant::*[text()='"+strLabel2+"']//ancestor-or-self::*[contains(@class,'field-label')]//following-sibling::*[contains(@class,'field-input')]//a");
		dialogButton=By.xpath("(//*[normalize-space(text())='"+strLabel1+"']//ancestor-or-self::*[contains(@id,'dialog') and @role='dialog' or contains(@class,'dialog')]//descendant::button[text()='"+strLabel2+"'])|(//*[normalize-space(text())='"+strLabel1+"']//ancestor::table//td/button[text()='"+strLabel2+"'])");
		surveyChoice=By.xpath("//*[(contains(@class,'hidden'))]//descendant::*[contains(@id,'category')]//label[text()='"+strLabel1+"']//ancestor-or-self::*[contains(@class,'table')]//descendant::img[@alt='"+strLabel2+"']");
		dropDownSelect = By.xpath("//*[text()='"+strLabel1+"']//parent::div//following::div//ul[@class='dropdown-menu']//li//a[text()='"+strLabel2+"']");
		glideLookupSearch = By.xpath("//*[text()='"+strLabel1+"']//ancestor::div[@type='glide_list']//following-sibling::div//span[text()='"+strLabel2+"']//ancestor::button");
		arrowBtn = By.xpath("//*[text()='"+strLabel1+"' and contains(@class,'label')]//ancestor-or-self::*[contains(@id,'element')]//descendant::*//img[@data-original-title='"+strLabel2+"']//ancestor::a[@id]");
		imgIcon = By.xpath("//*[text()='"+strLabel1+"']//ancestor::div[@type='glide_list' or @type='reference']//following-sibling::div//*[@data-original-title='"+strLabel2+"']");
		dialogRadioButton=By.xpath(".//label[contains(text(),'"+strLabel1+"')]//ancestor::table[@role='presentation']//span[@role='radio']//label[@class='radio-label' and text()='"+strLabel2+"']");
		comments = By.xpath("//span[contains(text(),'"+strLabel1+"')]//ancestor::li[contains(@class,'card_comments')]//span[text()='"+strLabel2+"']//ancestor::ul[contains(@class,'list-table')]//li");	
		tableHeaderSearch = By.xpath("//*[text()='"+strLabel1+"']//ancestor::div[@role='toolbar']//following-sibling::div//table[contains(@id,'table') or contains(@id,'report') and not(contains(@id,'clone'))]//thead//tr//td[@name='"+strLabel2+"']//input");
		//siva 04/01
		spDropDownButton = By.xpath("(.//*[contains(text(),'"+strLabel1+"')]//parent::div//*[contains(text(),'" + strLabel2 + "')])[last()]");
		spDropDownButtonVal = By.xpath(".//*[contains(text(),'"+strLabel1+"')]//parent::div[contains(@class,'watchlist')]//*[contains(@class,'" + strLabel2 + "')]");
		//siva 07/01
		dialogDropDown=By.xpath("//*[normalize-space(text())='"+strLabel1+"']//ancestor-or-self::*[contains(@id,'dialog') and @role='dialog' or contains(@class,'dialog')]//*[text()='"+strLabel2+"']//ancestor::div[contains(@class,'form-group')]//select");
		dialogTextBox=By.xpath("//*[normalize-space(text())='"+strLabel1+"']//ancestor-or-self::*[contains(@id,'dialog') and @role='dialog' or contains(@class,'dialog')]//*[text()='"+strLabel2+"']//following::div//input");
		dialogTextArea = By.xpath("(//*[normalize-space(text())='"+strLabel1+"']//ancestor-or-self::*[@role='dialog' or contains(@class,'dialog')]//*[text()='"+strLabel2+"']//following::td//textarea)|(//*[normalize-space(text())='"+strLabel1+"']//ancestor-or-self::*[@role='dialog' or contains(@class,'dialog')]//label[text()='"+strLabel2+"']//following::textarea)");
		
	}

	public Common(String strLabel1, String strLabel2, String strLabel3) {

		navigationbutton = By.xpath("//*[text()='" + strLabel1
				+ "']//ancestor-or-self::a[@role='button']//ancestor::li//descendant::*[text()='" + strLabel2 + "']"
				+ "//ancestor-or-self::a//following-sibling::*[contains(@class,'list')]//descendant::*[text()='"
				+ strLabel3 + "']//ancestor-or-self::a");
		
		checkbox=By.xpath("//*[normalize-space(text())='" + strLabel1 + "']//ancestor-or-self::*[contains(@class,'dialog')]"
				+ "//descendant::*[text()='" + strLabel2 + "']//following::*[text()='" + strLabel3 + "']//preceding-sibling::input[@type='checkbox']");
		
	}
	 public static final By infoIcon=By.xpath("(//button[contains(@class,'icon-info')])[last()]");
	public static final By homePageValidation = By.xpath("(//*[text()='ServiceNow Home Page'])|(//*[@aria-label='Home page'])");
	//public static final By tableHeader = By.xpath("//table[contains(@id,'table') and not(contains(@id,'clone'))]//thead/tr/th");
	//public static final By tableHeader = By.xpath("//table[contains(@id,'table') or contains(@id,'report') and not(contains(@id,'clone'))]//thead/tr/th");
	public static final By tableHeader = By.xpath("//div[@style='display: block;' and @aria-hidden='false']//table[contains(@id,'table') or contains(@id,'report') and not(contains(@id,'clone'))]//thead/tr/th[@role='columnheader']");
	public static final By navigationTableHeader = By.xpath("//div[@style='display: block;']//table[contains(@id,'table') or contains(@id,'report') and not(contains(@id,'clone'))]//thead/tr/th[@role='columnheader']");
	public static final By sparcTableHeader = By.xpath("//table[contains(@class,'table')]//thead/tr/th[contains(@class,'col')]");
	//public static final By tableRow = By.xpath("//table[contains(@id,'table') and not(contains(@id,'clone'))]//tbody/tr[contains(@class,'row')]");
	public static final By tableRow = By.xpath("//table[contains(@id,'table') or contains(@id,'report') and not(contains(@id,'clone'))]//tbody/tr[contains(@class,'row') or @role='row' and not(@aria-hidden)]");
	public static final By tableRowCMDB = By.xpath("//table[contains(@id,'table') or contains(@id,'report') and not(contains(@id,'clone'))]//tbody/tr[contains(@class,'row') and @record_class='u_configuration_task' or @role='row' and not(@aria-hidden)]");
	public static final By tableHeader_1 = By.xpath("//table[contains(@id,'table') and not(contains(@id,'clone'))]//thead/tr/th");
	public static final By tableHeader_2 = By.xpath("//table[contains(@id,'report') and not(contains(@id,'clone'))]//thead/tr/th");
	public static final By nextPage=By.xpath("//*[contains(@class,'list_nav_bottom')]//button[contains(@name,'next') and not(contains(@class,'disabled'))]");
	public static final By firstPage=By.xpath("//*[contains(@class,'list_nav_bottom')]//button[contains(@title,'First page')  and not(contains(@class,'disabled'))]");
	public static final By frame=By.xpath("//iframe[contains(@id,'main')]");
	public static final By cardFrame = By.xpath("//iframe[@class='card']");
	public static final By surveyFrame = By.xpath("//iframe[@id='survey_frame']");
	//public static final By UserMenu=By.xpath("//*[contains(@class,'header-content')]//descendant::button[contains(@id,'dropdown') and @aria-label='User menu']");
	//Siva 11/12
	public static final By UserMenu = By.xpath("//*[local-name()='button' or local-name()='a' or local-name()='span'][contains(@id,'dropdown') and contains(@aria-label,'User menu') or contains(@class,'dropdown') and @aria-label='User Profile' or (@title='User options')]");
	//public static final By UserMenu = By.xpath("//*[local-name()='button' or local-name()='a' or local-name()='span'][contains(@id,'dropdown') and @aria-label='User menu' or contains(@class,'dropdown') and @aria-label='User Profile' or (@title='User options')]");
	
	//public static final By Usermenulist=By.xpath("//*[contains(@class,'header-content')]//descendant::*[contains(@class,'dropdown-menu') and @role='menu']//li");
	public static final By Usermenulist=By.xpath("//*[contains(@class,'dropdown') and contains(@class,'open')]//descendant::ul[contains(@class,'dropdown-menu')]//descendant::li[not(@style)]");
	
	public static final By tooltipUserMenu=By.xpath("//*[contains(@class,'tooltip-inner')]");
	//siva 01/02
	public static final By searchTextbox=By.xpath("(//*[contains(@class,'active')]//descendant::input[@role='combobox' and contains(@class,'input') and contains(@id,'search')])[last()]");
	public static final By userMenuLabel=By.xpath("//*[@title='User menu' or @aria-label='User Profile' or @title='User options']//*[contains(@class,'user-name') or contains(@class,'visible')]");
	public static final By tableCheckbox=By.xpath(".//td//span[contains(@class,'checkbox')]");
	public static final By homePageLoadingIcon=By.xpath("//*[contains(@class,'loading icon')]");
	public static final By comboboxUserMenu=By.xpath("//ul[contains(@class,'sub') or @role='listbox']//li[contains(@class,'select')]");
	public static final By errorMessage=By.xpath("//*[contains(@class,'error') or @role='alert']//*[contains(@class,'notification') and not(contains(@class,'icon')) and not(contains(@class,'close')) or contains(@class,'output')]");
	public static final By lookupItemList=By.xpath("//a[@role='button' and contains(@class,'item_link') or @role='treeitem']");
	public static final By commentsList=By.xpath("//ul[contains(@class,'activities')]//li[contains(@class,'comments')]");
//	public static final By dateSelection=By.xpath("//table[@role='grid']//tr//td[contains(@class,'CurrentMonthDate')]");
	//public static final By dateMonthOkButton=By.xpath("//button[contains(@id,'ok') and contains(@class,'icon')]");
	public static final By monthPicker=By.xpath("//*[contains(@id,'DateTimePicker_month') and contains(@class,'Month') and  contains(@class,'Text')]");
	public static final By DatePickerNextbutton=By.xpath("//*[contains(@id,'next') and @role='button']");
	public static final By DatePickerPreviousbutton=By.xpath("//*[contains(@id,'prev') and @role='button']");
	public static final By allAppsTab=By.xpath("//a[contains(@id,'allApps_tab')]");
	public static final By filterNavigationButton=By.xpath("//*[@role='button' and contains(@class,'filter')]");
	public static final By envelope=By.xpath("//*[contains(@class,'envelope')]");
	public static final By mailItemList=By.xpath("//*[contains(@class,'list-item Emailed')]");
	public static final By successMessage=By.xpath("//*[contains(@class,'alert-msg')]");
	public static final By backButton=By.xpath("//*[text()='Back']//ancestor-or-self::button");
	public static final By mailBody=By.xpath("//*[@role='tabpanel' and contains(@class,'active') and contains(@id,'content')]");
	public static final By surveySucessMsg=By.xpath("//*[contains(@class,'description-text')]");
	//Siva 3-Jan-2022
	public static final By sparcPortTextbox=By.xpath("//*[(contains(@class,'hidden')) or (contains(@class,'search-bar')) or (contains(@class,'input-group'))]//descendant::input[@type='text' and @role='textbox']");

	/*public static final By sparcPortTextbox=By.xpath("//*[(contains(@class,'hidden')) or (contains(@class,'search-bar'))]//descendant::input[@type='text' and @role='textbox']");*/
	public static final By sparcPortSearchButton=By.xpath("//*[(contains(@class,'hidden')) or (contains(@class,'search-bar'))]//descendant::button[@name='search' or @title='Search']");
	public static final By languageListItem=By.xpath("//ul[contains(@class,'dropdown-menu') and @role='menu']//descendant::li[@role='menuitem']");
	public static final By articleContent=By.xpath("//*[contains(@class,'article-content')]//section");
	public static final By kbTitleHeader=By.xpath("//*[contains(@class,'kb-title-header')]");
	public static final By avaliLanguageLink=By.xpath("//a[contains(@aria-label,'language')]");
	public static final By filterNavigation=By.xpath("//input[@id='filter' and @type='search']");
	public static final By globalSearch=By.xpath("//input[contains(@aria-label,'Global Search') and @type='search']");
	public static final By globalSearchButton=By.xpath("//form[contains(@aria-label,'Global Search') and @role='search']");
	public static final By Usergrouptitle=By.xpath("//*[contains(@class,'navbar-title') and not(@draggable)]");
	public static final By ActionButton=By.xpath("//button[@aria-label='action bar' and contains(@class,'btn')]");
	public static final By tblLink=By.xpath(".//descendant::*[local-name()='th' or local-name()='td']//a[contains(@class,'formlink')]");
	public static final By tblLinkupdated=By.xpath(".//descendant::*[local-name()='th' or local-name()='td']//a[contains(@class,'formlink') or @class='linked']");
	public static final By tableLinkNew=By.xpath("//table[contains(@id,'table') or contains(@id,'report') and not(contains(@id,'clone'))]//tbody/tr[contains(@class,'row') or @role='row' and not(@aria-hidden)]//td/a[contains(@aria-label,'Approved')]");
	public static final By dropDownSearch = By.xpath("//div[contains(@class,'drop-active') and contains(@style,'display: block;')]//input[@role='combobox']");
	public static final By status = By.xpath("//div[@role='status']//span[@role='button']");
	//Supriya Jun 8th 21
	//*[contains(@class,'global-search') and @role="search"]
	
	public static final By searchBar1 = By.xpath("//*[contains(@class,'global-search') and @role=\"search\"]");
	public static final By searchtextValue = By.xpath("//*[contains(@id,'sysparm_search') and @type=\"search\"]");

	//*[contains(@id,'sysparm_search') and @type="search"]
	
	
	//Savithri March 31'2021
	public static final By rightArrow = By.xpath("//a[@role='button' and @data-original-title='Add' or contains(@class,'select')]"); //modified for combo box search
	public static final By downArrowIcon = By.xpath("//span[contains(@class,'arrow')]");
	public static final By addBtn = By.xpath("//div[@class='button-column']//a[@role='button' and @data-original-title='Add']");
	public static final By norecord = By.xpath("//table//tbody//tr[contains(@class,'records')]//td"); // modified with td on May 4th
	public static final By activityText = By.xpath("//div[contains(text(),'Activities')]//parent::div[contains(@class,'control-label')]");
	//public static final By homePageLogo = By.xpath("(//*[text()='ServiceNow Home Page'])//parent::a[@class='navbar-brand']");
	//Amit 06/01
	public static final By homePageLogo=By.xpath("(//*[text()='ServiceNow Home Page' or @title='SPARC Portal'])//parent::a[contains(@class,'navbar-brand')]");
	//Aparna 29-12-21 
  	//public static final By notificationLinkText = By.xpath("//div[@role='region']//div[contains(@class,'notification')]//a[@rel='nofollow']|//tr[contains(@id,'row_kb_knowledge')]//td[3]/a");
  	//Aparna 14/02
	public static final By notificationLinkText = By.xpath("//div[@role='region']//div[contains(@class,'notification')]//a[@rel='nofollow']|(//tr[contains(@id,'row_kb_knowledge')]//td[3]/a)|(//table[contains(@class,'status-table')]//td//a)");
	//public static final By notificationLinkText = By.xpath("//div[@role='region']//div[contains(@class,'notification')]//a[@rel='nofollow']");
	public static final By containerSearch = By.xpath("//input[@class='search-input']");
	public static final By readonlyNumber = By.xpath("//div[@id='articleNumberReadonly']");  
	public static final By alertMsg = By.xpath("//div[@role='alert']//span[@class='outputmsg_text']");
	public static final By submitBtn = By.xpath("//button[@type='submit']");
	public static final By errorLinkText = By.xpath("//*[@class='article']//a");
	public static final By clickLink = By.xpath("//tr[1][contains(@class,'list_row')]//td[3]//a");
	//siva 04/01
	public static final By getNumText = By.xpath(".//div[@aria-label='Page breadcrumbs']//li[@class='ng-scope']//a");
	public static final By attachmentIcon = By.xpath("//button[contains(@id,'attachment')]");
	public static final By choosefileBtn = By.xpath("//input[@value='Choose file'][@type='button']");
	public static final By closeBtn = By.xpath("//span[text()='Close']//parent::button[contains(@id,'attachment') or contains(@id,'popup')]");
	public static final By multiDropdown = By.xpath("//div[contains(@class,'drop-multi')]");
	public static final By highlightedList = By.xpath("//div[@role='option']//parent::li[contains(@class,'highlighted')]");
	public static final By tableBodyData = By.xpath("//table[@class='body_Class']//tr");
	//public static final By dialogPopup = By.xpath("//div[@role='dialog' and contains(@id,'popup')]//div[@class='modal-content']");
	//Aparna
	public static final By dialogPopup = By.xpath("(//div[@role='dialog' and contains(@id,'popup') or contains(@class,'modal-dialog')])[last()]//div[@class='modal-content']");
	//Aparna 04/01
	public static final By dialogFrame=By.xpath("//iframe[contains(@class,'iframe') or @id='dialog_frame']");
	//public static final By dialogFrame=By.xpath("//iframe[contains(@class,'iframe')]");
	public static final By dialogCloseButton=By.xpath(".//*[@aria-label='Close' and @role='button']");
	public static final By datePickerHour=By.xpath(".//*[contains(text(),'Time:')]//div/input[contains(@aria-label,'Hour')]");
	public static final By datePickerMinute=By.xpath(".//*[contains(text(),'Time:')]//div/input[contains(@aria-label,'Minute')]");
	public static final By datePickerSecond=By.xpath(".//*[contains(text(),'Time:')]//div/input[contains(@aria-label,'Second')]");
	//siva 01/02
	public static final By incidentFormValues = By.xpath(".//select[@aria-label='Collection'  or @aria-label='Available']");
	public static final By customizedSelectedDropdown = By.xpath(".//select[@aria-label='Selected']");
	public static final By personalizationButton = By.xpath("(.//*[text()='Update Personalized List']//parent::i[@role='button' and not(@aria-hidden='true')])|(.//*[text()='Personalize List']//parent::i[@role='button' and not(@aria-hidden='true')]) ");
	public static final By impersonateBackGroundPopup = By.xpath(".//div[contains(@class,'background')]//button[contains(@class,'close')]");
//	public static final By personalizeButton = By.xpath(".//*[text()='Personalize List']//parent::i[@role='button' and not(@aria-hidden='true')] ");
	public static final By filterButton=By.xpath("//*[@role='button' and contains(@class,'filter') and @aria-expanded='false']");
	//Amit 31-Dec-2021
	public static final By Articleframe=By.xpath("//iframe[@title='Article body']");
			//added Amit 29-12-2021
	public static final By attachment=By.xpath("(//a[@class='attachment' and text()='Click to add...'])[last()]");
	public static final By chooseflButton=By.xpath("//input[@id='attachFile']");
	public static final By OKButton=By.xpath("//input[@value='OK']");
	
	//Aparna 29-Dec-2021
    public static final By verifyApprove = By.xpath("//div[contains(@id,'context_list') and contains(@class,'context_menu')]//*[text()='Approve']");
//Yash 29-Dec-2021
    public static final By clickApprove= By.xpath("//div[text()='Approve']");
    public static final By notificationlinkText=By.xpath("(//tr[(contains(@id,'row_dmn_demand'))])[last()]//td[4]//a");
    //Supriya 04/01
    public static final By SparcEmailbox=By.xpath("//*[@role='menuitem' and contains(@class,'my-msgs')]");
   
    public static final By Sum=By.xpath("//*[text()='Sum']/following-sibling::td/span");
    public static final By YesButton=By.xpath("//button[contains(@id,'ok') and @type='button' ]");
   // public static final By infoIcon=By.xpath("(//button[contains(@class,'icon-info')])[last()]");
    public static final By Sparc_New_Portal_HeartIcon_xpath=By.xpath("//div[@class='search-item-fav-link']//*[@class='ng-scope fa fa-heart fav-inactive' and @role='button']");
  //siva 12/01
    public static final By dateSelection=By.xpath("//table[@role='grid']//tr//td[contains(@class,'Current')]");
    public static final By dateMonthOkButton=By.xpath("(//button[contains(@id,'ok') and contains(@class,'icon')])[last()]");
    public static final By errormsg = By.xpath("//div[text()='Security prevents writing to this field']");
    public static final By placeholder=By.xpath("//input[@placeholder='Add tag...']");
    
    public static final By tableBreadcrumbLink=By.xpath(".//button[@class='breadcrumb_link']//ancestor::div[@aria-hidden='false']//b");
    public static final By totalRows=By.xpath(".//span[contains(@id,'total_rows')]");
    public static final By onHold=By.xpath("//*[@id='OnHold']//img");
}

		
		
		
		
