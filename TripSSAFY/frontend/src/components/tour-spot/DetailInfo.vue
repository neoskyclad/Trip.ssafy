<template>
  <div class="space-y-4 text-sm text-gray-800">
    <!-- 관광지 -->
    <div v-if="info.useseason" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">이용시기</span>
      <p>{{ info.useseason }}</p>
    </div>

    <div v-if="info.usetime" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">이용시간</span>
      <p v-html="info.usetime"></p>
    </div>

    <div v-if="info.restdate" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">쉬는 날</span>
      <p v-html="info.restdate"></p>
    </div>

    <div v-if="info.opendate" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">개장일</span>
      <p v-html="info.opendate"></p>
    </div>

    <div v-if="info.infocenter" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">문의 안내</span>
      <p v-html="info.infocenter"></p>
    </div>

    <div v-if="info.expguide" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">체험 안내</span>
      <p v-html="info.expguide"></p>
    </div>
    <!-- 문화시설 -->
    <div v-if="info.usetimeculture" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">이용시간</span>
      <p v-html="info.usetimeculture"></p>
    </div>
    <div v-if="info.spendtime" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">관람소요시간</span>
      <p v-html="info.spendtime"></p>
    </div>
    <div v-if="info.restdateculture" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">쉬는 날</span>
      <p v-html="info.restdateculture"></p>
    </div>
    <div v-if="info.usefee" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">이용요금</span>
      <p v-html="info.usefee"></p>
    </div>
    <div v-if="info.parkingculture" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주차시설</span>
      <p v-html="info.parkingculture"></p>
    </div>
    <div v-if="info.parkingfee" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주차요금</span>
      <p v-html="info.parkingfee"></p>
    </div>
    <div v-if="info.infocenterculture" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">문의 및 안내</span>
      <p v-html="info.infocenterculture"></p>
    </div>
    <!-- 행사 -->
    <div v-if="info.bookingplace" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">예매처</span>
      <p v-html="info.bookingplace"></p>
    </div>
    <div v-if="info.eventstartdate" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">행사 기간</span>
      <p>{{ info.eventstartdate }}~{{ info.eventenddate }}</p>
    </div>
    <div v-if="info.eventhomepage" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">홈페이지</span>
      <p v-html="info.eventhomepage"></p>
    </div>
    <div v-if="info.eventplace" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">행사 장소</span>
      <p v-html="info.eventplace"></p>
    </div>
    <div v-if="info.sponsor2" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주관사</span>
      <p v-html="info.sponsor2"></p>
    </div>
    <div v-if="info.sponsor2tel" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주관사연락처</span>
      <p v-html="info.sponsor2tel"></p>
    </div>
    <div v-if="info.playtime" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">공연 시간</span>
      <p v-html="info.playtime"></p>
    </div>
    <div v-if="info.program" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">이용 요금</span>
      <p v-html="info.program"></p>
    </div>
    <div v-if="info.program" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">행사 프로그램</span>
      <p v-html="info.program"></p>
    </div>
    <!-- 여행 코스 -->
    <div v-if="info.distance" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">코스 총 거리</span>
      <p v-html="info.distance"></p>
    </div>
    <div v-if="info.schedule" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">코스 일정</span>
      <p v-html="info.schedule"></p>
    </div>
    <div v-if="info.infocentertourcourse" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">문의 및 안내</span>
      <p v-html="info.infocentertourcourse"></p>
    </div>
    <div v-if="info.taketime" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">총 소요시간</span>
      <p v-html="info.taketime"></p>
    </div>
    <!-- 레포츠 -->
    <div v-if="info.infocenterleports" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">문의 및 안내</span>
      <p v-html="info.infocenterleports"></p>
    </div>
    <div v-if="info.openperiod" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">개장 기간</span>
      <p v-html="info.openperiod"></p>
    </div>
    <div v-if="info.parkingleports" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주차시설</span>
      <p v-html="info.parkingleports"></p>
    </div>
    <div v-if="info.parkingfeeleports" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주차요금</span>
      <p v-html="info.parkingfeeleports"></p>
    </div>
    <div v-if="info.restdateleports" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">쉬는 날</span>
      <p v-html="info.restdateleports"></p>
    </div>
    <div v-if="info.usetimeleports" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">이용 시간</span>
      <p v-html="info.usetimeleports"></p>
    </div>
    <div v-if="info.usefeeleports" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">입장료</span>
      <p v-html="info.usefeeleports"></p>
    </div>
    <!-- 숙박 -->
    <div v-if="info.reservationurl" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">홈페이지</span>
      <p v-html="info.reservationurl"></p>
    </div>
    <div v-if="info.roomtype" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">객실 유형</span>
      <p v-html="info.roomtype"></p>
    </div>
    <div v-if="info.checkintime" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">입실 시간</span>
      <p v-html="info.checkintime"></p>
    </div>
    <div v-if="info.checkouttime" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">퇴실 시간</span>
      <p v-html="info.checkouttime"></p>
    </div>
    <div v-if="info.infocenterlodging" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">문의 및 안내</span>
      <p v-html="info.infocenterlodging"></p>
    </div>
    <div v-if="info.parkinglodging" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주차시설</span>
      <p v-html="info.parkinglodging"></p>
    </div>
    <div v-if="info.subfacility" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">부대시설</span>
      <p v-html="info.subfacility"></p>
    </div>
    <div v-if="info.refundregulation" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">환불 규정</span>
      <p v-html="info.refundregulation"></p>
    </div>
    <!-- 쇼핑 -->
    <div v-if="info.culturecenter" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">문화센터바로가기</span>
      <p v-html="info.culturecenter"></p>
    </div>
    <div v-if="info.infocentershopping" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">문의 및 안내</span>
      <p v-html="info.infocentershopping"></p>
    </div>
    <div v-if="info.opendateshopping" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">개장일</span>
      <p v-html="info.opendateshopping"></p>
    </div>
    <div v-if="info.opentime" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">영업 시간</span>
      <p v-html="info.opentime"></p>
    </div>
    <div v-if="info.restdateshopping" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">쉬는 날</span>
      <p v-html="info.restdateshopping"></p>
    </div>
    <div v-if="info.parkingshopping" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주차시설</span>
      <p v-html="info.parkingshopping"></p>
    </div>
    <div v-if="info.saleitem" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">판매품목</span>
      <p v-html="info.saleitem"></p>
    </div>
    <div v-if="info.saleitemcost" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">품목 별 가격</span>
      <p v-html="info.saleitemcost"></p>
    </div>
    <div v-if="info.shopguide" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">매장 안내</span>
      <p v-html="info.shopguide"></p>
    </div>
    <!-- 음식점 -->
    <div v-if="info.opentimefood" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">영업시간</span>
      <p v-html="info.opentimefood"></p>
    </div>
    <div v-if="info.restdatefood" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">쉬는 날</span>
      <p v-html="info.restdatefood"></p>
    </div>
    <div v-if="info.infocenterfood" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">문의 및 안내</span>
      <p v-html="info.infocenterfood"></p>
    </div>
    <div v-if="info.reservationfood" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">예약안내</span>
      <p v-html="info.reservationfood"></p>
    </div>
    <div v-if="info.firstmenu" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">대표메뉴</span>
      <p v-html="info.firstmenu"></p>
    </div>
    <div v-if="info.treatmenu" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">취급메뉴</span>
      <p v-html="info.treatmenu"></p>
    </div>
    <div v-if="info.parkingfood" class="flex gap-2 items-start">
      <span class="font-semibold w-24 text-blue-500">주차시설</span>
      <p v-html="info.parkingfood"></p>
    </div>
    <div class="w-full flex gap-1 overflow-x-auto">
      <img
        v-for="imageurl in imageUrls"
        :src="imageurl.smallimageurl"
        class="max-h-[100px]"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { showToastError } from "@/api/toastMsg";
const props = defineProps({
  contentId: Number,
  contentTypeId: Number,
});
const info = ref({});
const imageUrls = ref([]);
const loadImages = async () => {
  try {
    const response = await axios.get(
      `http://localhost:8080/tour-spot/image?contentId=${props.contentId}`
    );
    imageUrls.value = response.data.response.body.items.item;
    console.log(imageUrls.value);
  } catch (err) {
    showToastError("상세 정보 불러오기 실패!");
    console.log(err);
  }
};
onMounted(async () => {
  try {
    const response = await axios.get(
      `http://localhost:8080/tour-spot/detail?contentId=${props.contentId}&contentTypeId=${props.contentTypeId}`
    );
    info.value = response.data.response.body.items.item[0];
    loadImages();
  } catch (err) {
    showToastError("상세 정보 불러오기 실패!");
    console.log(err);
  }
});
</script>

<style scoped></style>
