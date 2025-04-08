import{B as H,J as _,K as N,y as u,L as U,o,c as l,a as I,M as Z,I as b,N as D,O as S,P as m,t as P,Z as x,Q as y,R as g,S as A,T as W,U as j,V as C,W as q,X as J,Y as E,w as L,b as v,$ as Q,z as w,F as h,A as K,d as R,a0 as F,e as X,a1 as Y,C as G,a2 as $}from"./CgXyU8S8.js";import{R as ee,U as T,C as te}from"./OtOWOkM3.js";import{O as ne}from"./bYOJpIkd.js";var ie=function(e){var n=e.dt;return`
.p-menu {
    background: `.concat(n("menu.background"),`;
    color: `).concat(n("menu.color"),`;
    border: 1px solid `).concat(n("menu.border.color"),`;
    border-radius: `).concat(n("menu.border.radius"),`;
    min-width: 12.5rem;
}

.p-menu-list {
    margin: 0;
    padding: `).concat(n("menu.list.padding"),`;
    outline: 0 none;
    list-style: none;
    display: flex;
    flex-direction: column;
    gap: `).concat(n("menu.list.gap"),`;
}

.p-menu-item-content {
    transition: background `).concat(n("menu.transition.duration"),", color ").concat(n("menu.transition.duration"),`;
    border-radius: `).concat(n("menu.item.border.radius"),`;
    color: `).concat(n("menu.item.color"),`;
}

.p-menu-item-link {
    cursor: pointer;
    display: flex;
    align-items: center;
    text-decoration: none;
    overflow: hidden;
    position: relative;
    color: inherit;
    padding: `).concat(n("menu.item.padding"),`;
    gap: `).concat(n("menu.item.gap"),`;
    user-select: none;
    outline: 0 none;
}

.p-menu-item-label {
    line-height: 1;
}

.p-menu-item-icon {
    color: `).concat(n("menu.item.icon.color"),`;
}

.p-menu-item.p-focus .p-menu-item-content {
    color: `).concat(n("menu.item.focus.color"),`;
    background: `).concat(n("menu.item.focus.background"),`;
}

.p-menu-item.p-focus .p-menu-item-icon {
    color: `).concat(n("menu.item.icon.focus.color"),`;
}

.p-menu-item:not(.p-disabled) .p-menu-item-content:hover {
    color: `).concat(n("menu.item.focus.color"),`;
    background: `).concat(n("menu.item.focus.background"),`;
}

.p-menu-item:not(.p-disabled) .p-menu-item-content:hover .p-menu-item-icon {
    color: `).concat(n("menu.item.icon.focus.color"),`;
}

.p-menu-overlay {
    box-shadow: `).concat(n("menu.shadow"),`;
}

.p-menu-submenu-label {
    background: `).concat(n("menu.submenu.label.background"),`;
    padding: `).concat(n("menu.submenu.label.padding"),`;
    color: `).concat(n("menu.submenu.label.color"),`;
    font-weight: `).concat(n("menu.submenu.label.font.weight"),`;
}

.p-menu-separator {
    border-block-start: 1px solid `).concat(n("menu.separator.border.color"),`;
}
`)},se={root:function(e){var n=e.props;return["p-menu p-component",{"p-menu-overlay":n.popup}]},start:"p-menu-start",list:"p-menu-list",submenuLabel:"p-menu-submenu-label",separator:"p-menu-separator",end:"p-menu-end",item:function(e){var n=e.instance;return["p-menu-item",{"p-focus":n.id===n.focusedOptionId,"p-disabled":n.disabled()}]},itemContent:"p-menu-item-content",itemLink:"p-menu-item-link",itemIcon:"p-menu-item-icon",itemLabel:"p-menu-item-label"},oe=H.extend({name:"menu",theme:ie,classes:se}),re={name:"BaseMenu",extends:_,props:{popup:{type:Boolean,default:!1},model:{type:Array,default:null},appendTo:{type:[String,Object],default:"body"},autoZIndex:{type:Boolean,default:!0},baseZIndex:{type:Number,default:0},tabindex:{type:Number,default:0},ariaLabel:{type:String,default:null},ariaLabelledby:{type:String,default:null}},style:oe,provide:function(){return{$pcMenu:this,$parentInstance:this}}},V={name:"Menuitem",hostName:"Menu",extends:_,inheritAttrs:!1,emits:["item-click","item-mousemove"],props:{item:null,templates:null,id:null,focusedOptionId:null,index:null},methods:{getItemProp:function(e,n){return e&&e.item?N(e.item[n]):void 0},getPTOptions:function(e){return this.ptm(e,{context:{item:this.item,index:this.index,focused:this.isItemFocused(),disabled:this.disabled()}})},isItemFocused:function(){return this.focusedOptionId===this.id},onItemClick:function(e){var n=this.getItemProp(this.item,"command");n&&n({originalEvent:e,item:this.item.item}),this.$emit("item-click",{originalEvent:e,item:this.item,id:this.id})},onItemMouseMove:function(e){this.$emit("item-mousemove",{originalEvent:e,item:this.item,id:this.id})},visible:function(){return typeof this.item.visible=="function"?this.item.visible():this.item.visible!==!1},disabled:function(){return typeof this.item.disabled=="function"?this.item.disabled():this.item.disabled},label:function(){return typeof this.item.label=="function"?this.item.label():this.item.label},getMenuItemProps:function(e){return{action:u({class:this.cx("itemLink"),tabindex:"-1","aria-hidden":!0},this.getPTOptions("itemLink")),icon:u({class:[this.cx("itemIcon"),e.icon]},this.getPTOptions("itemIcon")),label:u({class:this.cx("itemLabel")},this.getPTOptions("itemLabel"))}}},directives:{ripple:ee}},ae=["id","aria-label","aria-disabled","data-p-focused","data-p-disabled"],le=["href","target"];function ue(t,e,n,s,r,i){var f=U("ripple");return i.visible()?(o(),l("li",u({key:0,id:n.id,class:[t.cx("item"),n.item.class],role:"menuitem",style:n.item.style,"aria-label":i.label(),"aria-disabled":i.disabled()},i.getPTOptions("item"),{"data-p-focused":i.isItemFocused(),"data-p-disabled":i.disabled()||!1}),[I("div",u({class:t.cx("itemContent"),onClick:e[0]||(e[0]=function(d){return i.onItemClick(d)}),onMousemove:e[1]||(e[1]=function(d){return i.onItemMouseMove(d)})},i.getPTOptions("itemContent")),[n.templates.item?n.templates.item?(o(),b(S(n.templates.item),{key:1,item:n.item,label:i.label(),props:i.getMenuItemProps(n.item)},null,8,["item","label","props"])):m("",!0):Z((o(),l("a",u({key:0,href:n.item.url,class:t.cx("itemLink"),target:n.item.target,tabindex:"-1"},i.getPTOptions("itemLink")),[n.templates.itemicon?(o(),b(S(n.templates.itemicon),{key:0,item:n.item,class:D(t.cx("itemIcon"))},null,8,["item","class"])):n.item.icon?(o(),l("span",u({key:1,class:[t.cx("itemIcon"),n.item.icon]},i.getPTOptions("itemIcon")),null,16)):m("",!0),I("span",u({class:t.cx("itemLabel")},i.getPTOptions("itemLabel")),P(i.label()),17)],16,le)),[[f]])],16)],16,ae)):m("",!0)}V.render=ue;function z(t){return pe(t)||me(t)||ce(t)||de()}function de(){throw new TypeError(`Invalid attempt to spread non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}function ce(t,e){if(t){if(typeof t=="string")return M(t,e);var n={}.toString.call(t).slice(8,-1);return n==="Object"&&t.constructor&&(n=t.constructor.name),n==="Map"||n==="Set"?Array.from(t):n==="Arguments"||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)?M(t,e):void 0}}function me(t){if(typeof Symbol<"u"&&t[Symbol.iterator]!=null||t["@@iterator"]!=null)return Array.from(t)}function pe(t){if(Array.isArray(t))return M(t)}function M(t,e){(e==null||e>t.length)&&(e=t.length);for(var n=0,s=Array(e);n<e;n++)s[n]=t[n];return s}var B={name:"Menu",extends:re,inheritAttrs:!1,emits:["show","hide","focus","blur"],data:function(){return{id:this.$attrs.id,overlayVisible:!1,focused:!1,focusedOptionIndex:-1,selectedOptionIndex:-1}},watch:{"$attrs.id":function(e){this.id=e||T()}},target:null,outsideClickListener:null,scrollHandler:null,resizeListener:null,container:null,list:null,mounted:function(){this.id=this.id||T(),this.popup||(this.bindResizeListener(),this.bindOutsideClickListener())},beforeUnmount:function(){this.unbindResizeListener(),this.unbindOutsideClickListener(),this.scrollHandler&&(this.scrollHandler.destroy(),this.scrollHandler=null),this.target=null,this.container&&this.autoZIndex&&x.clear(this.container),this.container=null},methods:{itemClick:function(e){var n=e.item;this.disabled(n)||(n.command&&n.command(e),this.overlayVisible&&this.hide(),!this.popup&&this.focusedOptionIndex!==e.id&&(this.focusedOptionIndex=e.id))},itemMouseMove:function(e){this.focused&&(this.focusedOptionIndex=e.id)},onListFocus:function(e){this.focused=!0,!this.popup&&this.changeFocusedOptionIndex(0),this.$emit("focus",e)},onListBlur:function(e){this.focused=!1,this.focusedOptionIndex=-1,this.$emit("blur",e)},onListKeyDown:function(e){switch(e.code){case"ArrowDown":this.onArrowDownKey(e);break;case"ArrowUp":this.onArrowUpKey(e);break;case"Home":this.onHomeKey(e);break;case"End":this.onEndKey(e);break;case"Enter":case"NumpadEnter":this.onEnterKey(e);break;case"Space":this.onSpaceKey(e);break;case"Escape":this.popup&&(y(this.target),this.hide());case"Tab":this.overlayVisible&&this.hide();break}},onArrowDownKey:function(e){var n=this.findNextOptionIndex(this.focusedOptionIndex);this.changeFocusedOptionIndex(n),e.preventDefault()},onArrowUpKey:function(e){if(e.altKey&&this.popup)y(this.target),this.hide(),e.preventDefault();else{var n=this.findPrevOptionIndex(this.focusedOptionIndex);this.changeFocusedOptionIndex(n),e.preventDefault()}},onHomeKey:function(e){this.changeFocusedOptionIndex(0),e.preventDefault()},onEndKey:function(e){this.changeFocusedOptionIndex(g(this.container,'li[data-pc-section="item"][data-p-disabled="false"]').length-1),e.preventDefault()},onEnterKey:function(e){var n=A(this.list,'li[id="'.concat("".concat(this.focusedOptionIndex),'"]')),s=n&&A(n,'a[data-pc-section="itemlink"]');this.popup&&y(this.target),s?s.click():n&&n.click(),e.preventDefault()},onSpaceKey:function(e){this.onEnterKey(e)},findNextOptionIndex:function(e){var n=g(this.container,'li[data-pc-section="item"][data-p-disabled="false"]'),s=z(n).findIndex(function(r){return r.id===e});return s>-1?s+1:0},findPrevOptionIndex:function(e){var n=g(this.container,'li[data-pc-section="item"][data-p-disabled="false"]'),s=z(n).findIndex(function(r){return r.id===e});return s>-1?s-1:0},changeFocusedOptionIndex:function(e){var n=g(this.container,'li[data-pc-section="item"][data-p-disabled="false"]'),s=e>=n.length?n.length-1:e<0?0:e;s>-1&&(this.focusedOptionIndex=n[s].getAttribute("id"))},toggle:function(e){this.overlayVisible?this.hide():this.show(e)},show:function(e){this.overlayVisible=!0,this.target=e.currentTarget},hide:function(){this.overlayVisible=!1,this.target=null},onEnter:function(e){W(e,{position:"absolute",top:"0",left:"0"}),this.alignOverlay(),this.bindOutsideClickListener(),this.bindResizeListener(),this.bindScrollListener(),this.autoZIndex&&x.set("menu",e,this.baseZIndex+this.$primevue.config.zIndex.menu),this.popup&&y(this.list),this.$emit("show")},onLeave:function(){this.unbindOutsideClickListener(),this.unbindResizeListener(),this.unbindScrollListener(),this.$emit("hide")},onAfterLeave:function(e){this.autoZIndex&&x.clear(e)},alignOverlay:function(){j(this.container,this.target);var e=C(this.target);e>C(this.container)&&(this.container.style.minWidth=C(this.target)+"px")},bindOutsideClickListener:function(){var e=this;this.outsideClickListener||(this.outsideClickListener=function(n){var s=e.container&&!e.container.contains(n.target),r=!(e.target&&(e.target===n.target||e.target.contains(n.target)));e.overlayVisible&&s&&r?e.hide():!e.popup&&s&&r&&(e.focusedOptionIndex=-1)},document.addEventListener("click",this.outsideClickListener))},unbindOutsideClickListener:function(){this.outsideClickListener&&(document.removeEventListener("click",this.outsideClickListener),this.outsideClickListener=null)},bindScrollListener:function(){var e=this;this.scrollHandler||(this.scrollHandler=new te(this.target,function(){e.overlayVisible&&e.hide()})),this.scrollHandler.bindScrollListener()},unbindScrollListener:function(){this.scrollHandler&&this.scrollHandler.unbindScrollListener()},bindResizeListener:function(){var e=this;this.resizeListener||(this.resizeListener=function(){e.overlayVisible&&!q()&&e.hide()},window.addEventListener("resize",this.resizeListener))},unbindResizeListener:function(){this.resizeListener&&(window.removeEventListener("resize",this.resizeListener),this.resizeListener=null)},visible:function(e){return typeof e.visible=="function"?e.visible():e.visible!==!1},disabled:function(e){return typeof e.disabled=="function"?e.disabled():e.disabled},label:function(e){return typeof e.label=="function"?e.label():e.label},onOverlayClick:function(e){ne.emit("overlay-click",{originalEvent:e,target:this.target})},containerRef:function(e){this.container=e},listRef:function(e){this.list=e}},computed:{focusedOptionId:function(){return this.focusedOptionIndex!==-1?this.focusedOptionIndex:null}},components:{PVMenuitem:V,Portal:J}},fe=["id"],he=["id","tabindex","aria-activedescendant","aria-label","aria-labelledby"],be=["id"];function ve(t,e,n,s,r,i){var f=E("PVMenuitem"),d=E("Portal");return o(),b(d,{appendTo:t.appendTo,disabled:!t.popup},{default:L(function(){return[v(Q,u({name:"p-connected-overlay",onEnter:i.onEnter,onLeave:i.onLeave,onAfterLeave:i.onAfterLeave},t.ptm("transition")),{default:L(function(){return[!t.popup||r.overlayVisible?(o(),l("div",u({key:0,ref:i.containerRef,id:r.id,class:t.cx("root"),onClick:e[3]||(e[3]=function(){return i.onOverlayClick&&i.onOverlayClick.apply(i,arguments)})},t.ptmi("root")),[t.$slots.start?(o(),l("div",u({key:0,class:t.cx("start")},t.ptm("start")),[w(t.$slots,"start")],16)):m("",!0),I("ul",u({ref:i.listRef,id:r.id+"_list",class:t.cx("list"),role:"menu",tabindex:t.tabindex,"aria-activedescendant":r.focused?i.focusedOptionId:void 0,"aria-label":t.ariaLabel,"aria-labelledby":t.ariaLabelledby,onFocus:e[0]||(e[0]=function(){return i.onListFocus&&i.onListFocus.apply(i,arguments)}),onBlur:e[1]||(e[1]=function(){return i.onListBlur&&i.onListBlur.apply(i,arguments)}),onKeydown:e[2]||(e[2]=function(){return i.onListKeyDown&&i.onListKeyDown.apply(i,arguments)})},t.ptm("list")),[(o(!0),l(h,null,K(t.model,function(a,c){return o(),l(h,{key:i.label(a)+c.toString()},[a.items&&i.visible(a)&&!a.separator?(o(),l(h,{key:0},[a.items?(o(),l("li",u({key:0,id:r.id+"_"+c,class:[t.cx("submenuLabel"),a.class],role:"none",ref_for:!0},t.ptm("submenuLabel")),[w(t.$slots,t.$slots.submenulabel?"submenulabel":"submenuheader",{item:a},function(){return[R(P(i.label(a)),1)]})],16,be)):m("",!0),(o(!0),l(h,null,K(a.items,function(p,O){return o(),l(h,{key:p.label+c+"_"+O},[i.visible(p)&&!p.separator?(o(),b(f,{key:0,id:r.id+"_"+c+"_"+O,item:p,templates:t.$slots,focusedOptionId:i.focusedOptionId,unstyled:t.unstyled,onItemClick:i.itemClick,onItemMousemove:i.itemMouseMove,pt:t.pt},null,8,["id","item","templates","focusedOptionId","unstyled","onItemClick","onItemMousemove","pt"])):i.visible(p)&&p.separator?(o(),l("li",u({key:"separator"+c+O,class:[t.cx("separator"),a.class],style:p.style,role:"separator",ref_for:!0},t.ptm("separator")),null,16)):m("",!0)],64)}),128))],64)):i.visible(a)&&a.separator?(o(),l("li",u({key:"separator"+c.toString(),class:[t.cx("separator"),a.class],style:a.style,role:"separator",ref_for:!0},t.ptm("separator")),null,16)):(o(),b(f,{key:i.label(a)+c.toString(),id:r.id+"_"+c,item:a,index:c,templates:t.$slots,focusedOptionId:i.focusedOptionId,unstyled:t.unstyled,onItemClick:i.itemClick,onItemMousemove:i.itemMouseMove,pt:t.pt},null,8,["id","item","index","templates","focusedOptionId","unstyled","onItemClick","onItemMousemove","pt"]))],64)}),128))],16,he),t.$slots.end?(o(),l("div",u({key:1,class:t.cx("end")},t.ptm("end")),[w(t.$slots,"end")],16)):m("",!0)],16,fe)):m("",!0)]}),_:3},16,["onEnter","onLeave","onAfterLeave"])]}),_:3},8,["appendTo","disabled"])}B.render=ve;const ye={class:"w-[200px] bg-neutral-200"},k="/resources",ge=F({__name:"index",props:{fullPath:{}},setup(t){const e=t,n=[{label:"Головне",to:`${k}`},{label:"Працівники",to:`${k}/employees`},{label:"Послуги",to:`${k}/services`},{label:"Заходи та ціни",to:`${k}/events`}];return(s,r)=>{const i=X,f=B;return o(),l("aside",ye,[r[0]||(r[0]=I("div",{class:"text-lg font-semibold mt-6 mb-2 mx-3"},"Моя організація",-1)),v(f,{model:n},{item:L(({item:d})=>[v(i,{class:D([{"!text-primary-base rounded":e.fullPath===d.to},"p-menu-item-link"]),to:d.to},{default:L(()=>[R(P(d.label),1)]),_:2},1032,["class","to"])]),_:1})])}}}),Oe=F({__name:"resources",setup(t){const e=Y();return(n,s)=>{const r=ge,i=$;return o(),l(h,null,[v(r,{"full-path":G(e).fullPath},null,8,["full-path"]),v(i)],64)}}});export{Oe as default};
