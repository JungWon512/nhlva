<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<!-- <div class="monitoring-head"> -->
<!-- 	<h2>농협 가축시장 전자경매시스템</h2> -->
<!-- <!-- 	<p>2021.04.10 - 256회차</p> --> 
<!-- </div> -->
<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
<input type="hidden" id="userId" value="${userId}" />

<input type="hidden" id="tokenNm" value="${tokenNm}" />
<input type="hidden" id="token" value="${token}" />
<div class="table-inner">
	<div class="table-info">
		<ul>
			<li>
				<div class="tb-box">
					<table class="bill-table">
						<colgroup>
							<col width="16%">
							<col width="16%">
							<col width="12%">
							<col width="12%">
							<col width="12%">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<td class="td-ico" rowspan="2"><div class="ico">스마트 경매</div></td>
								<td class="bg-deep"><p class="">경매일자</p></td>
								<td class="bg-deep"><p class="">경매</p></td>
<!-- 								<td class="bg-deep"><p class="">구분</p></td> -->
								<td class="bg-deep"><p class="">출장우</p></td>
								<td class="bg-dark" rowspan="2" colspan="2">
									<p class="tit1">접속 <span class="num">0</span>명</p>
									<p class="tit2">응찰 <span class="num">0</span>명</p>
								</td>
							</tr>
							<tr>
								<td class="bg-blue"><p class="">${today }</p></td>
								<td class="bg-blue"><p class="">${johapData.AUC_DSC eq '1' ? '단일' : '일괄'}</p></td>
<!-- 								<td class="bg-blue"><p class="">큰소경매</p></td> -->
								<td class="bg-blue"><p class="">${auctCount.CNT }두</p></td>
							</tr>
						</tbody>
					</table>
				</div>
			</li>
			<li>
				<div class="tb-box">
					<table class="bill-table">
						<colgroup>
							<col width="33%">
							<col width="33%">
							<col width="33%">
						</colgroup>
						<tbody>
							<tr>
								<td class="mt-gray"><button id="btnStart">모니터링<br>시작</button></td>
								<td class="mt-gray"><button id="btnStop">모니터링<br>중지</button></td>
								<td class="mt-gray"><button id="btnSort">매수인<br>정렬</button></td>
							</tr>
						</tbody>
					</table>
				</div>
			</li>
		</ul>
	</div>
	<!-- table-info -->
	<div class="table-mt">
		<div class="mt-tb-inner">
			<table class="mt-table connMnTable">
				<colgroup>
					<col width="10%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
					<col width="10%">
				</colgroup>
				<tbody>
					<tr>
						<td class="">
							<p>1</p>
						</td>
						<td class="">
							<p>2</p>
						</td>
						<td class="">
							<p>3</p>
						</td>
						<td class="">
							<p>4</p>
						</td>
						<td class="">
							<p>5</p>
						</td>
						<td class="">
							<p>6</p>
						</td>
						<td class="">
							<p>7</p>
						</td>
						<td class="">
							<p>8</p>
						</td>
						<td class="">
							<p>9</p>
						</td>
						<td class="">
							<p>10</p>
						</td>
					</tr>
					<tr>
						<td class="">
							<p>11</p>
						</td>
						<td class="">
							<p>12</p>
						</td>
						<td class="">
							<p>13</p>
						</td>
						<td class="">
							<p>14</p>
						</td>
						<td class="">
							<p>15</p>
						</td>
						<td class="">
							<p>16</p>
						</td>
						<td class="">
							<p>17</p>
						</td>
						<td class="">
							<p>18</p>
						</td>
						<td class="">
							<p>19</p>
						</td>
						<td class="">
							<p>20</p>
						</td>
					</tr>
					<tr>
						<td class="">
							<p>21</p>
						</td>
						<td class="">
							<p>22</p>
						</td>
						<td class="">
							<p>23</p>
						</td>
						<td class="">
							<p>24</p>
						</td>
						<td class="">
							<p>25</p>
						</td>
						<td class="">
							<p>26</p>
						</td>
						<td class="">
							<p>27</p>
						</td>
						<td class="">
							<p>28</p>
						</td>
						<td class="">
							<p>29</p>
						</td>
						<td class="">
							<p>30</p>
						</td>
					</tr>
					<tr>
						<td class="">
							<p>31</p>
						</td>
						<td class="">
							<p>32</p>
						</td>
						<td class="">
							<p>33</p>
						</td>
						<td class="">
							<p>34</p>
						</td>
						<td class="">
							<p>35</p>
						</td>
						<td class="">
							<p>36</p>
						</td>
						<td class="">
							<p>37</p>
						</td>
						<td class="">
							<p>38</p>
						</td>
						<td class="">
							<p>39</p>
						</td>
						<td class="">
							<p>40</p>
						</td>
					</tr>
					<tr>
						<td class="">
							<p>41</p>
						</td>
						<td class="">
							<p>42</p>
						</td>
						<td class="">
							<p>43</p>
						</td>
						<td class="">
							<p>44</p>
						</td>
						<td class="">
							<p>45</p>
						</td>
						<td class="">
							<p>46</p>
						</td>
						<td class="">
							<p>47</p>
						</td>
						<td class="">
							<p>48</p>
						</td>
						<td class="">
							<p>49</p>
						</td>
						<td class="">
							<p>50</p>
						</td>
					</tr>
					<tr>
						<td class="">
							<p>51</p>
						</td>
						<td class="">
							<p>52</p>
						</td>
						<td class="">
							<p>53</p>
						</td>
						<td class="">
							<p>54</p>
						</td>
						<td class="">
							<p>55</p>
						</td>
						<td class="">
							<p>56</p>
						</td>
						<td class="">
							<p>57</p>
						</td>
						<td class="">
							<p>58</p>
						</td>
						<td class="">
							<p>59</p>
						</td>
						<td class="">
							<p>60</p>
						</td>
					</tr>
					<tr>
						<td>
							<p>61</p>
						</td>
						<td>
							<p>62</p>
						</td>
						<td>
							<p>63</p>
						</td>
						<td>
							<p>64</p>
						</td>
						<td>
							<p>65</p>
						</td>
						<td>
							<p>66</p>
						</td>
						<td>
							<p>67</p>
						</td>
						<td>
							<p>68</p>
						</td>
						<td>
							<p>69</p>
						</td>
						<td>
							<p>70</p>
						</td>
					</tr>
					<tr>
						<td>
							<p>71</p>
						</td>
						<td>
							<p>72</p>
						</td>
						<td>
							<p>73</p>
						</td>
						<td>
							<p>74</p>
						</td>
						<td>
							<p>75</p>
						</td>
						<td>
							<p>76</p>
						</td>
						<td>
							<p>77</p>
						</td>
						<td>
							<p>78</p>
						</td>
						<td>
							<p>79</p>
						</td>
						<td>
							<p>80</p>
						</td>
					</tr>
					<tr>
						<td>
							<p>81</p>
						</td>
						<td>
							<p>82</p>
						</td>
						<td>
							<p>83</p>
						</td>
						<td>
							<p>84</p>
						</td>
						<td>
							<p>85</p>
						</td>
						<td>
							<p>86</p>
						</td>
						<td>
							<p>87</p>
						</td>
						<td>
							<p>88</p>
						</td>
						<td>
							<p>89</p>
						</td>
						<td>
							<p>90</p>
						</td>
					</tr>
					<tr>
						<td>
							<p>91</p>
						</td>
						<td>
							<p>92</p>
						</td>
						<td>
							<p>93</p>
						</td>
						<td>
							<p>94</p>
						</td>
						<td>
							<p>95</p>
						</td>
						<td>
							<p>96</p>
						</td>
						<td>
							<p>97</p>
						</td>
						<td>
							<p>98</p>
						</td>
						<td>
							<p>99</p>
						</td>
						<td>
							<p>100</p>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>