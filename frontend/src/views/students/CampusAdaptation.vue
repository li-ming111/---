<template>
  <div class="page-container">
    <h1>校园适应</h1>
    <CampusAdaptation />
  </div>
</template>

<script>
import CampusAdaptation from '../../components/CampusAdaptation.vue'

export default {
  components: {
    CampusAdaptation
  }
}
</script>

<style scoped>
.page-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 30px;
  min-height: 600px;
}

h1 {
  color: #333;
  margin-bottom: 30px;
  font-size: 24px;
  font-weight: 600;
  border-bottom: 2px solid #1976D2;
  padding-bottom: 10px;
}
</style>
