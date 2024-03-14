<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<c:choose>
    <c:when test="${empty inputParam.searchAucObjDsc}">
        <div class="sum_table">
            <div>
                <dl>
                    <dt><p>구분</p></dt>
                    <dd><p>송아지</p></dd>
                    <dd><p>비육우</p></dd>
                    <dd><p>번식우</p></dd>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '5')}">
                    <dd><p>염소</p></dd>
                </c:if>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '6')}">
                    <dd><p>말</p></dd>
                </c:if>
                    <dd><p>합계</p></dd>
                </dl>
                <dl>
                    <dt><p>전체</p></dt>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_CALF}</span>두</p>
                    </dd>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_NO_COW}</span>두</p>
                    </dd>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_COW}</span>두</p>
                    </dd>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '5')}">
                    <dd><p><span class="ea">${buyCnt.CNT_5}</span>두</p></dd>
                </c:if>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '6')}">
                    <dd><p><span class="ea">${buyCnt.CNT_6}</span>두</p></dd>
                </c:if>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT}</span>두</p>
                    </dd>
                </dl>
                <dl>
                    <dt><p>암</p></dt>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_W_F_1}</span>두</p>
                    </dd>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_W_F_2}</span>두</p>
                    </dd>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_W_F_3}</span>두</p>
                    </dd>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '5')}">
                    <dd><p><span class="ea">${buyCnt.CNT_SEX_W_F_5}</span>두</p></dd>
                </c:if>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '6')}">
                    <dd><p><span class="ea">${buyCnt.CNT_SEX_W_F_6}</span>두</p></dd>
                </c:if>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_W_F}</span>두</p>
                    </dd>
                </dl>
                <dl>
                    <dt><p>수</p></dt>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_M_F_1}</span>두</p>
                    </dd>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_M_F_2}</span>두</p>
                    </dd>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_M_F_3}</span>두</p>
                    </dd>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '5')}">
                    <dd><p><span class="ea">${buyCnt.CNT_SEX_M_F_5}</span>두</p></dd>
                </c:if>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '6')}">
                    <dd><p><span class="ea">${buyCnt.CNT_SEX_M_F_6}</span>두</p></dd>
                </c:if>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_M_F}</span>두</p>
                    </dd>
                </dl>
                <dl>
                    <dt><p>기타</p></dt>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_ETC_F_1}</span>두</p>
                    </dd>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_ETC_F_2}</span>두</p>
                    </dd>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_ETC_F_3}</span>두</p>
                    </dd>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '5')}">
                    <dd><p><span class="ea">${buyCnt.CNT_SEX_ETC_F_5}</span>두</p></dd>
                </c:if>
                <c:if test="${fn:contains(johapData.ETC_AUC_OBJ_DSC, '6')}">
                    <dd><p><span class="ea">${buyCnt.CNT_SEX_ETC_F_6}</span>두</p></dd>
                </c:if>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_ETC_F}</span>두</p>
                    </dd>
                </dl>
            </div>
        </div> 
    </c:when>
    <c:otherwise>
        <div class="sum_table">
            <div>
                <dl>
                    <dt><p>전체</p></dt>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT}</span>두</p>
                    </dd>
                </dl>
                <dl>
                    <dt><p>암</p></dt>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_W_F}</span>두</p>
                    </dd>
                </dl>
                <dl>
                    <dt><p>수</p></dt>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_M_F}</span>두</p>
                    </dd>
                </dl>
                <dl>
                    <dt><p>기타</p></dt>
                    <dd>
                        <p><span class="ea">${buyCnt.CNT_SEX_ETC_F}</span>두</p>
                    </dd>
                </dl>
            </div>
        </div>
    </c:otherwise>
    </c:choose>