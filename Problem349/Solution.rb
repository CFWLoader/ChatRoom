# @param {Integer[]} nums1
# @param {Integer[]} nums2
# @return {Integer[]}
def intersection(nums1, nums2)

  nums1.sort!

  nums2.sort!

  idx1 = 0; bound1 = nums1.size
  idx2 = 0; bound2 = nums2.size

  intersection_set = []

  while idx1 < bound1 and idx2 < bound2

    if nums1[idx1] < nums2[idx2]
      while idx1 < bound1 and nums1[idx1] < nums2[idx2]
        idx1 += 1
      end
    elsif nums1[idx1] > nums2[idx2]
      while idx2 < bound2 and nums1[idx1] > nums2[idx2]
        idx2 += 1
      end
    end

    count = 0

    compare_val = nums1[idx1]

    while idx1 < bound1 and idx2 < bound2 and nums1[idx1] == compare_val and nums2[idx2] == compare_val
      idx1 += 1; idx2 += 1; count += 1
    end

    unless count <= 0 then intersection_set << compare_val end

  end

  intersection_set

end

# print intersection [1, 2, 2, 1], [2, 2]
#
# puts
#
# print intersection [1], [1, 1]
#
# puts
#
# print intersection [1, 2], [1, 1]
#
# puts
#
# print intersection [2, 1], [1, 2]
#
# puts

print intersection [4, 9, 5], [9, 4, 9, 8, 4]

puts
